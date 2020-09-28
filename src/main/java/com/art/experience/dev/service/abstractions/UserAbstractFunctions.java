package com.art.experience.dev.service.abstractions;

import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.exception.CreateResourceException;
import com.art.experience.dev.model.Barber;
import com.art.experience.dev.model.Client;
import com.art.experience.dev.model.DTO.DTOBarberResponse;
import com.art.experience.dev.model.DTO.DTOClientResponse;
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

    private final Long FIRST_SOCIAL_NUMBER = 1L;

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
                //TODO: CREAR Logica para ocultar la informacion crucial del barbero.
                DTOBarberResponse secureBarber = barberService.findByUserId(userResponse.getUserId());
                //DTOBarberResponse secureBarber = decoratorPatternBarber(Optional.of(barberResult));

                /** @add: Set Barber data for the Response*/
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
                Client clientResult = clientService.findByUserId(userResponse.getUserId());

                //TODO: CREAR Logica para ocultar la informacion crucial del cliente.
                DTOClientResponse secureClient = decoratorPatternClient(Optional.of(clientResult));

                /** @add: Set Client data for the Response*/
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
        user.setUser(userResponse);
        return user;
    }

    public DTOBarberResponse decoratorPatternBarber(Optional<Barber> barberResult) {
        LOGGER.info("Starting filtering. . . . ");
        DTOBarberResponse barberSecure = new DTOBarberResponse();
        if (barberResult.isPresent()) {
            //todo: SET DTO Barber
            LOGGER.info("filtering. . . . ");
            Barber brb = barberResult.get();
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
        LOGGER.info("Returning. . . . " + barberSecure.getBarberId());
        return barberSecure;
    }

    protected DTOClientResponse decoratorPatternClient(Optional<Client> clientResult) {
        DTOClientResponse clientSecure = new DTOClientResponse();
        if (clientResult.isPresent()) {
            //TODO: SET DTO CLIENT
            Client cli = clientResult.get();
            clientSecure.setClientId(cli.getClientId());
            clientSecure.setUserId(cli.getUserId());
            clientSecure.setEmail(cli.getEmail());
            clientSecure.setUsername(cli.getUsername());
            clientSecure.setName(cli.getName());
            clientSecure.setStatus(cli.getStatus());
        }
        return clientSecure;
    }

    public User createGenericUser(final Optional<User> user, final Optional<Client> client, final Optional<Barber> barber) {
        try {
            User newUser = createOrUpdateUserFromClientOrBarberData(user, client, barber);

            LOGGER.info("Start Username Validation");
            Optional<User> usernameValidation = userRepository.findByUsername(newUser.getUsername());
            if (usernameValidation.isPresent()) {
                LOGGER.error(newUser.getUsername() + " already exists, please try with another Username.");
                throw new CreateResourceException(newUser.getUsername() + " already exists, please try with another Username.");
            } else {
                LOGGER.info("Start Email Validation");
                Optional<User> emailValidationUser = userRepository.findByEmail(newUser.getEmail());
                if (emailValidationUser.isPresent()) {
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
            if (checkUser.isEmpty()) {
                LOGGER.info("User ID Not found with the next ID: " + userId.get());
                throw new CreateResourceException("User ID Not found with the next ID: " + userId.get());
            } else {
                newUser = createOrUpdateUserFromClientOrBarberData(user, client, barber);
                newUser.setCreateOn(checkUser.get().getCreateOn());
                newUser.setEmail(Objects.isNull(newUser.getEmail()) ? checkUser.get().getEmail() : newUser.getEmail());
            }

            LOGGER.info("Start Username Validation");
            Optional<User> usernameValidation = userRepository.findByUsername(newUser.getUsername());
            if (!usernameValidation.isEmpty()) {
                if (!usernameValidation.get().getUserId().equals(newUser.getUserId())) {
                    LOGGER.error(newUser.getUsername() + " already exists, please try with another Username.");
                    throw new CreateResourceException(newUser.getUsername() + " already exists, please try with another Username.");
                } else {
                    LOGGER.info("The Username is available!");
                }
            } else {
                LOGGER.info("Start Email Validation");
                Optional<User> emailValidationUser = userRepository.findByEmail(newUser.getUsername());
                if (!emailValidationUser.isEmpty()) {
                    if (!emailValidationUser.get().getUserId().equals(newUser.getUserId())) {
                        LOGGER.error(newUser.getUsername() + " already exists, please try with another Email.");
                        throw new CreateResourceException(newUser.getUsername() + " already exists, please try with another Email.");
                    } else {
                        LOGGER.info("The Email is available!");
                    }
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
        if (!user.isEmpty()) {
            LOGGER.info("User Is Not present! Creating user info . . .");

            //TODO: VALIDAR QUE EL MAIL DEL CLIENTE QUE SE ESTE CREANDO EN ESTE MOMENTO NO EXISTA EN LA BASE DE DATOS
            //      TAMBIEN VALIDAR EL USERNAME, QUE NO EXISTA EN LA BASE DE DATOS.

            newUser.setUserId(Objects.nonNull(user.get().getUserId()) ? user.get().getUserId() : null);
            newUser.setUsername(user.get().getUsername());
            newUser.setPassword(user.get().getPassword());
            newUser.setAdmin(!Objects.isNull(user.get().isAdmin()));
            newUser.setBarber(!Objects.isNull(user.get().isBarber()));
            newUser.setCreateOn(Objects.isNull(user.get().getCreateOn()) ? Instant.now() : user.get().getCreateOn());

            if (Objects.isNull(client.get().getSocialNumber())) {
                Long maxValue = userRepository.getLatestSocialNumber();
                if (Objects.isNull(maxValue)) {
                    LOGGER.info("Set first social Number!!!! " + FIRST_SOCIAL_NUMBER);
                    newUser.setSocialNumber(FIRST_SOCIAL_NUMBER);
                } else {
                    newUser.setSocialNumber(maxValue + 1);
                }
            } else {
                newUser.setSocialNumber(client.get().getSocialNumber());
            }

            if (Objects.nonNull(user.get().getDeleteOn())) {
                user.get().setDeleteOn(Instant.now());
                user.get().setStatus(false);
            } else {
                newUser.setStatus(true);
            }

            LOGGER.info("User created Directly!");
        } else if (!client.isEmpty()) {
            LOGGER.info("Client User Is Not present! Creating Client User info . . .");

            //TODO: VALIDAR QUE EL MAIL DEL CLIENTE QUE SE ESTE CREANDO EN ESTE MOMENTO NO EXISTA EN LA BASE DE DATOS
            //      TAMBIEN VALIDAR EL USERNAME, QUE NO EXISTA EN LA BASE DE DATOS.
            try {

                Client verifyClient = clientService.getByEmail(client.get().getEmail());

                if (Objects.nonNull(verifyClient.getEmail())) {
                    LOGGER.error(verifyClient.getEmail() + " already exists, please try with another Email.");
                    throw new ResourceNotFoundException(verifyClient.getEmail() + " already exists, please try with another Email.");

                } else if (verifyClient.getUsername().equals(client.get().getUsername())) {
                    LOGGER.error(verifyClient.getUsername() + " already exists, please try with another Username.");
                    throw new ResourceNotFoundException(verifyClient.getUsername() + " already exists, please try with another Username.");
                }

                LOGGER.info("The Email and username are available!");

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
                    Long maxValue = userRepository.getLatestSocialNumber();
                    if (Objects.isNull(maxValue)) {
                        LOGGER.info("Set first social Number!!!! " + FIRST_SOCIAL_NUMBER);
                        newUser.setSocialNumber(FIRST_SOCIAL_NUMBER);
                    } else {
                        LOGGER.info("Set social Number! latest + 1 " + (maxValue + 1));
                        newUser.setSocialNumber(maxValue + 1);
                    }
                } else {
                    LOGGER.info("Set social Number! " + client.get().getSocialNumber());
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
            } catch (Exception ex) {
                LOGGER.error("Error thrown on client creation. .", ex.getMessage());
                throw new CreateResourceException("Error thrown on client creation. ." + ex.getMessage());
            }

        } else if (!barber.isEmpty()) {
            LOGGER.info("Barber User Is Not present! Creating Barber User info . . .");
            newUser.setUserId(Objects.nonNull(barber.get().getUserId()) ? barber.get().getUserId() : null);
            newUser.setEmail(barber.get().getEmail());
            newUser.setUsername(barber.get().getUsername());
            newUser.setPassword(barber.get().getPassword());
            newUser.setStatus(true);
            newUser.setAdmin(true);
            newUser.setBarber(true);
            newUser.setCreateOn(Objects.isNull(barber.get().getStartDate()) ? Instant.now() : barber.get().getStartDate());

            // Set social Number
            // validate in case of update this number should be not empty
            if (Objects.isNull(client.get().getSocialNumber())) {
                Long maxValue = userRepository.getLatestSocialNumber();
                if (Objects.isNull(maxValue)) {
                    LOGGER.info("Set first social Number!!!! " + FIRST_SOCIAL_NUMBER);
                    newUser.setSocialNumber(FIRST_SOCIAL_NUMBER);
                } else {
                    LOGGER.info("Set Social Number: N° " + (maxValue + 1));
                    newUser.setSocialNumber(maxValue + 1);
                }
            } else {
                newUser.setSocialNumber(client.get().getSocialNumber());
            }

            // Check if Delete Date appear e.e
            if (Objects.nonNull(barber.get().getEndDate())) {
                newUser.setDeleteOn(Instant.now());
                newUser.setStatus(false);
            } else {
                newUser.setStatus(true);
            }
            LOGGER.info("Barber User Created!");
        }
        LOGGER.info("User Created Successfully!! -> User: " + newUser.getUserId());
        return newUser;
    }


}
