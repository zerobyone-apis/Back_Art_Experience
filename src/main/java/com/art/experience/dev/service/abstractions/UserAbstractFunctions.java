package com.art.experience.dev.service.abstractions;

import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.exception.CreateResourceException;
import com.art.experience.dev.model.Barber;
import com.art.experience.dev.model.Client;
import com.art.experience.dev.model.DTO.DTOBarberResponse;
import com.art.experience.dev.model.DTO.DTOClientResponse;
import com.art.experience.dev.model.DTO.DTOUserResponse;
import com.art.experience.dev.model.DTOUserLogin;
import com.art.experience.dev.model.User;
import com.art.experience.dev.service.BarberService;
import com.art.experience.dev.service.ClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class UserAbstractFunctions {

	private static final Logger LOGGER = LogManager.getLogger(UserAbstractFunctions.class);

	@Autowired
	private ClientService clientService;
	@Autowired
	private BarberService barberService;

	private final UserRepository userRepository;

	/**
	 * @param userRepository
	 * @Constructor <b>Barber</b>
	 */
	public UserAbstractFunctions(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	private final Long FIRST_SOCIAL_NUMBER = 600L;

	public DTOUserLogin loginUser(final DTOUserLogin user) {
		User userResponse = new User();
		try {
			if (Objects.nonNull(user.getSocialNumber())) {
				userResponse = userRepository.findBySocialNumberAndPassword(user.getSocialNumber(), user.getPassword()).orElse(null);
				if (Objects.isNull(userResponse)) {
					LOGGER.error("This Social Number " + user.getSocialNumber() + " or Password: " + user.getPassword() + " is not exist on the database.");
					throw new CreateResourceException("This Social Number " + user.getSocialNumber() + " or Password: " + user.getPassword() + " is not exist on the database.");
				}
			} else if (Objects.nonNull(user.getEmail())) {
				userResponse = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()).orElse(null);
				if (Objects.isNull(userResponse)) {
					LOGGER.error("This Email: " + user.getEmail() + " or Password: " + user.getPassword() + " is not exist on the database.");
					throw new CreateResourceException("This Email: " + user.getEmail() + " or Password: " + user.getPassword() + " is not exist on the database.");
				}
			}
		} catch (SQLGrammarException | CreateResourceException ex) {
			LOGGER.error("This Email: " + user.getEmail() + " or Password: " + user.getPassword() + " is not exist on the database.");
			throw new CreateResourceException("This Email: " + user.getEmail() + " or Password: " + user.getPassword() + " is not exist on the database.");
		}

		if (userResponse.isBarber() && userResponse.isAdmin()) {
			try {
				LOGGER.error("Step to set Barber on the result DTO User");
				DTOBarberResponse secureBarber = barberService.findByUserId(userResponse.getUserId());

				/** @add: Set Barber data for the Response */
				if (Objects.nonNull(secureBarber)) {
					user.setBarber(secureBarber);
				}
			} catch (Exception ex) {
				LOGGER.error("Error finding barber with User ID, Error message: " + ex.getMessage());
				throw new CreateResourceException("Error finding barber with User ID, Error message: " + ex.getMessage());
			} finally {
				LOGGER.error("The set Barber was Successfully!!");
			}

		} else {
			try {

				LOGGER.error("Step to set Client on the result DTO User");
				DTOClientResponse secureClient = clientService.findByUserId(userResponse.getUserId());

				/** @add: Set Client data for the Response */
				if (Objects.nonNull(secureClient)) {
					user.setClient(secureClient);
				}
			} catch (Exception ex) {
				LOGGER.error("Error finding client with User ID, Error message: " + ex.getMessage());
				throw new CreateResourceException("Error finding client with User ID, Error message: " + ex.getMessage());
			} finally {
				LOGGER.error("The set client was Successfully!!");
			}
		}

		// TODO: Crear una tabla de registro de incio de sessions , con los datos del usuario que inicio session y la hora fecha, tiempo.
		//      Para tener un tracking de los usuarios que inician session. (Sacar metricas)

		/** @add: Set User data for the Response*/
		user.setUser(decoratorPatternUser(userResponse));

		//Cleaning critical data
		user.setPassword("");
		Predicate<Long> socialN = Objects::nonNull;
		if (socialN.test(user.getSocialNumber())) {
			user.setSocialNumber(0L);
		} else {
			user.setEmail("");
		}
		return user;
	}

	public User createGenericUser(Optional<User> user, final Optional<Client> client, final Optional<Barber> barber) {
		try {
			User newUser = createOrUpdateUserFromClientOrBarberData(user, client, barber);

			LOGGER.info("Start Username Validation");
			Optional<User> username = userRepository.findByUsername(newUser.getUsername());
			if (username.isPresent()) {
				LOGGER.error(newUser.getUsername() + " already exists, please try with another Username.");
				throw new CreateResourceException(newUser.getUsername() + " already exists, please try with another Username.");
			} else {
				LOGGER.info("Start Email Validation");
				Optional<User> email = userRepository.findByEmail(newUser.getEmail());
				if (email.isPresent()) {
					LOGGER.error(newUser.getEmail() + " already exists, please try with another Email.");
					throw new CreateResourceException(newUser.getEmail() + " already exists, please try with another Email.");
				}
			}

			LOGGER.info("Finally, Checked User the Username and Email was validate Successfully!!");
			return userRepository.save(newUser);
		} catch (Exception ex) {
			LOGGER.error("Error on the user creation, error message: " + ex.getMessage());
			throw new CreateResourceException("Error on the user creation, error message: " + ex.getMessage());
		}
	}

	public User updateGenericUser(final Optional<User> user, final Optional<Client> client, final Optional<Barber> barber, Optional<Long> userId) {
		User newUser;
		try {
			LOGGER.info("Start User Exists Validation");
			Optional<User> checkUser = userRepository.findById(userId.get());
			if (checkUser.isPresent()) {
				newUser = createOrUpdateUserFromClientOrBarberData(user, client, barber);
				newUser.setCreateOn(checkUser.get().getCreateOn());
				newUser.setEmail(Objects.isNull(newUser.getEmail()) ? checkUser.get().getEmail() : newUser.getEmail());
			} else {
				LOGGER.info("User ID Not found with the next ID: " + userId.get());
				throw new CreateResourceException("User ID Not found with the next ID: " + userId.get());
			}

			LOGGER.info("Start Username Validation");
			Optional<User> username = userRepository.findByUsername(newUser.getUsername());
			if (username.isPresent()) {
				LOGGER.info("Start Email Validation");
				Optional<User> email = userRepository.findByEmail(newUser.getUsername());
				if (email.isPresent()) {
					if (!email.get().getUserId().equals(newUser.getUserId())) {
						LOGGER.error(newUser.getUsername() + " already exists, please try with another Email.");
						throw new CreateResourceException(newUser.getUsername() + " already exists, please try with another Email.");
					} else {
						LOGGER.info("The Email is available!");
					}
				}
			} else {
				if (!username.get().getUserId().equals(newUser.getUserId())) {
					LOGGER.error(newUser.getUsername() + " already exists, please try with another Username.");
					throw new CreateResourceException(newUser.getUsername() + " already exists, please try with another Username.");
				} else {
					LOGGER.info("The Username is available!");
				}
			}

			LOGGER.info("Finally, Checked User the Username and Email was validate Successfully!!");
			return userRepository.save(newUser);
		} catch (Exception ex) {
			LOGGER.error("Error on the user creation, error message: " + ex.getMessage());
			throw new CreateResourceException("Error on the user creation, error message: " + ex.getMessage());
		}
	}

	private User createOrUpdateUserFromClientOrBarberData(final Optional<User> user, final Optional<Client> client, final Optional<Barber> barber) {
		LOGGER.info("Start User Validation!");
		User newUser = new User();
		if (user.isPresent()) {
			LOGGER.info("User Is Not present! Creating user info . . .");

			newUser.setUserId(Objects.nonNull(user.get().getUserId()) ? user.get().getUserId() : null);
			newUser.setUsername(user.get().getUsername());
			newUser.setPassword(user.get().getPassword());
			newUser.setAdmin(!Objects.isNull(user.get().isAdmin()));
			newUser.setBarber(!Objects.isNull(user.get().isBarber()));
			newUser.setCreateOn(Objects.isNull(user.get().getCreateOn()) ? Instant.now() : user.get().getCreateOn());

			if (Objects.isNull(user.get().getSocialNumber())) {
				Long maxValue = userRepository.getLatestSocialNumber();
				if (Objects.isNull(maxValue)) {
					LOGGER.info("Set first social Number!!!! " + FIRST_SOCIAL_NUMBER);
					newUser.setSocialNumber(FIRST_SOCIAL_NUMBER);
				} else {
					newUser.setSocialNumber(maxValue + 1);
				}
			} else {
				newUser.setSocialNumber(user.get().getSocialNumber());
			}

			if (Objects.nonNull(user.get().getDeleteOn())) {
				user.get().setDeleteOn(Instant.now());
				user.get().setStatus(false);
			} else {
				newUser.setStatus(true);
			}

			LOGGER.info("User created Directly!");
		} else if (client.isPresent()) {
			LOGGER.info("Client User Is Not present! Creating Client User info . . .");

			newUser.setUserId(Objects.nonNull(client.get().getUserId()) ? client.get().getUserId() : null);
			newUser.setEmail(client.get().getEmail());
			newUser.setUsername(client.get().getUsername());
			newUser.setPassword(client.get().getPassword());
			newUser.setAdmin(false);
			newUser.setBarber(false);
			newUser.setCreateOn(Objects.isNull(client.get().getStartDate()) ? Instant.now() : client.get().getStartDate());

			// Set social Number
			// validate in case of update this number should be not empty
			if (Objects.isNull(client.get().getSocialNumber())) {
				// Max values are for current users not socials members.
				// FIRST_SOCIAL_NUMbER is to start
				// If we have social n° on client just put that Number.
				Long maxValue = userRepository.getLatestSocialNumber();
				if (Objects.isNull(maxValue)) {
					LOGGER.info("Set first social Number !!!! " + FIRST_SOCIAL_NUMBER);
					newUser.setSocialNumber(FIRST_SOCIAL_NUMBER);
				} else {
					LOGGER.info("Set latest social Number + 1 " + (maxValue + 1));
					if (maxValue >= 600) {
						newUser.setSocialNumber(maxValue + 1);
					} else {
						// we set te new social num on 601 'couse max social n° is low than 600
						LOGGER.info("Set First social Number + 1 " + (FIRST_SOCIAL_NUMBER + 1));
						newUser.setSocialNumber(FIRST_SOCIAL_NUMBER + 1);
					}
				}
			} else {
				LOGGER.info("Setting Client social Number!!! " + client.get().getSocialNumber());
				newUser.setSocialNumber(client.get().getSocialNumber());
			}

			// Check if Delete Date appear <(e.e- )7
			if (Objects.nonNull(client.get().getEndDate())) {
				newUser.setDeleteOn(Instant.now());
				newUser.setStatus(false);
			} else {
				newUser.setStatus(true);
			}
			LOGGER.info("Client User Created!");
		} else if (barber.isPresent()) {
			LOGGER.info("Barber User Is Not present! Creating Barber User info . . .");
			newUser.setUserId(Objects.nonNull(barber.get().getUserId()) ? barber.get().getUserId() : null);
			newUser.setEmail(barber.get().getEmail());
			newUser.setUsername(barber.get().getUsername());
			newUser.setPassword(barber.get().getPassword());
			newUser.setStatus(true);
			newUser.setAdmin(true);
			newUser.setBarber(true);
			newUser.setCreateOn(Objects.isNull(barber.get().getStartDate()) ? Instant.now() : barber.get().getStartDate());

			// Check if Delete Date appear e.e
			if (Objects.nonNull(barber.get().getEndDate())) {
				newUser.setDeleteOn(Instant.now());
				newUser.setStatus(false);
			} else {
				newUser.setStatus(true);
			}
			LOGGER.info("Barber User Created!");
		}
		LOGGER.info("User Created Successfully!! -> Social N°: " + newUser.getSocialNumber());
		return newUser;
	}

	//Decorators....
	protected DTOBarberResponse decoratorPatternBarber(Barber brb) {
		LOGGER.info("Starting Barber filter. . . ");
		DTOBarberResponse barberSecure = new DTOBarberResponse();
		if (Objects.nonNull(brb)) {

			//todo: SET DTO Barber
			LOGGER.info("filtering. . .  ");

			barberSecure.setBarber(brb.getActive());
			barberSecure.setAdmin(brb.getAdmin());
			barberSecure.setBarberId(brb.getBarberId());
			barberSecure.setUserId(brb.getUserId());
			barberSecure.setName(brb.getName());
			barberSecure.setBarberDescription(brb.getBarberDescription());
			barberSecure.setFacebook(brb.getFacebook());
			barberSecure.setInstagram(brb.getInstagram());
			barberSecure.setUrlProfileImage(brb.getUrlProfileImage());
			barberSecure.setPrestige(brb.getPrestige());

			barberSecure.setLocalName(brb.getLocalName());
			barberSecure.setUsername(brb.getUsername());
			barberSecure.setEmail(brb.getEmail());
		}
		LOGGER.info("Returning. . . " + barberSecure.getBarberId());
		return barberSecure;
	}

	protected DTOClientResponse decoratorPatternClient(Client clientResult) {
		DTOClientResponse clientSecure = new DTOClientResponse();
		if (Objects.nonNull(clientResult)) {
			//TODO: SET DTO CLIENT
			clientSecure.setClientId(clientResult.getClientId());
			clientSecure.setUserId(clientResult.getUserId());
			clientSecure.setEmail(clientResult.getEmail());
			clientSecure.setUsername(clientResult.getUsername());
			clientSecure.setName(clientResult.getName());
			clientSecure.setStatus(clientResult.getStatus());
			clientSecure.setSocialNumber(clientResult.getSocialNumber());
			clientSecure.setCel('0'+String.valueOf(clientResult.getCel()));
		}
		return clientSecure;
	}

	protected DTOUserResponse decoratorPatternUser(User user) {
		DTOUserResponse userSecure = new DTOUserResponse();
		if (Objects.nonNull(user)) {
			//TODO: SET DTO CLIENT
			userSecure.setEmail(user.getEmail());
			userSecure.setUsername(user.getUsername());
			userSecure.setSocialNumber(user.getSocialNumber());
			userSecure.setBarber(user.getBarber());
			userSecure.setStatus(user.getStatus());
			userSecure.setAdmin(user.getAdmin());
		}
		return userSecure;
	}

}
