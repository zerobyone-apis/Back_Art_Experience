package com.art.experience.dev.service.abstractions;

import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.exception.CreateResourceException;
import com.art.experience.dev.model.Barber;
import com.art.experience.dev.model.Client;
import com.art.experience.dev.model.DTOUserLogin;
import com.art.experience.dev.model.User;
import com.art.experience.dev.service.BarberService;
import com.art.experience.dev.service.ClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public abstract class UserAbstractFunctions {

    private static final Logger LOGGER = LogManager.getLogger(UserAbstractFunctions.class);

    @Autowired
    private ClientService clientService;
    @Autowired
    private BarberService barberService;

    private UserRepository userRepository;

    /**
     * @param userRepository
     * @Constructor <b>Barber</b>
     */
    public UserAbstractFunctions(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DTOUserLogin loginUser(final DTOUserLogin user) {
        final User userResponse;
        try {
            userResponse = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()).orElse(null);
            if (Objects.isNull(userResponse)) {
                LOGGER.error("This Email: " + user.getEmail() + " or Password: " + user.getPassword() + " is not exist on the database.");
                throw new CreateResourceException("This Email: " + user.getEmail() + " or Password: " + user.getPassword() + " is not exist on the database.");
            }
        } catch (SQLGrammarException | CreateResourceException ex) {
            LOGGER.error("This Email: " + user.getEmail() + " or Password: " + user.getPassword() + " is not exist on the database.");
            throw new CreateResourceException("This Email: " + user.getEmail() + " or Password: " + user.getPassword() + " is not exist on the database.");
        }

        if (userResponse.isBarber() && userResponse.isAdmin()) {
            try {
                LOGGER.error("Step to set Barber on the result DTO User");
                Barber barberResult = barberService.findByUserId(userResponse.getUserId());

                /** @add: Set Barber data for the Response*/
                user.setBarber(barberResult);
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

                /** @add: Set Client data for the Response*/
                user.setClient(clientResult);
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

    public User createGenericUser(final Optional<User> user, final Optional<Client> client, final Optional<Barber> barber) {
        User newUser = createOrUpdateUserFromClientOrBarberData(user, client, barber);
        try {
            LOGGER.error("Start Username Validation");
            Optional<User> usernameValidation = userRepository.findByUsername(newUser.getUsername());
            if (!usernameValidation.isEmpty()) {
                LOGGER.error(newUser.getUsername() + " already exists, please try with another Username.");
                throw new CreateResourceException(newUser.getUsername() + " already exists, please try with another Username.");
            } else {
                LOGGER.error("Start Email Validation");
                Optional<User> emailValidationUser = userRepository.findByEmail(newUser.getUsername());
                if (!emailValidationUser.isEmpty()) {
                    LOGGER.error(newUser.getEmail() + " already exists, please try with another Email.");
                    throw new CreateResourceException(newUser.getEmail() + " already exists, please try with another Email.");
                }
            }

            LOGGER.error("Finally, Checked User the Username and Email was validate Successfully!!");
            return userRepository.save(newUser);
        } catch (Exception ex) {
            LOGGER.error("Error on the user creation, error message: " + ex.getMessage());
            throw new CreateResourceException("Error on the user creation, error message: " + ex.getMessage());
        }
    }

    public User updateGenericUser(final Optional<User> user, final Optional<Client> client, final Optional<Barber> barber) {
        User newUser = createOrUpdateUserFromClientOrBarberData(user, client, barber);

        try {
            LOGGER.info("Start User Exists Validation");
            Optional<User> checkUser = userRepository.findById(newUser.getUserId());
            if (checkUser.isEmpty()) {
                LOGGER.info("User ID Not found with the next ID: " + newUser.getUserId());
                throw new CreateResourceException("User ID Not found with the next ID: " + newUser.getUserId());
            } else {
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
            newUser.setUserId(Objects.nonNull(user.get().getUserId()) ? user.get().getUserId() : null);
            newUser.setUsername(user.get().getUsername());
            newUser.setPassword(user.get().getPassword());
            newUser.setAdmin(Objects.isNull(user.get().isAdmin()) ? false : true);
            newUser.setBarber(Objects.isNull(user.get().isBarber()) ? false : true);
            newUser.setCreateOn(Objects.isNull(user.get().getCreateOn()) ? Instant.now() : user.get().getCreateOn());
            if (Objects.nonNull(user.get().getDeleteOn())) {
                user.get().setDeleteOn(Instant.now());
                user.get().setStatus(false);
            } else {
                newUser.setStatus(true);
            }
            LOGGER.info("User created Directly!");
        } else if (!client.isEmpty()) {
            LOGGER.info("Client User Is Not present! Creating Client User info . . .");
            newUser.setUserId(Objects.nonNull(client.get().getUserId()) ? client.get().getUserId() : null);
            newUser.setEmail(client.get().getEmail());
            newUser.setUsername(client.get().getUsername());
            newUser.setPassword(client.get().getPassword());
            newUser.setStatus(true);
            newUser.setAdmin(false);
            newUser.setBarber(false);
            LOGGER.error("Client User Created!");
        } else if (!barber.isEmpty()) {
            LOGGER.info("Barber User Is Not present! Creating Barber User info . . .");
            newUser.setUserId(Objects.nonNull(barber.get().getUserId()) ? barber.get().getUserId() : null);
            newUser.setEmail(barber.get().getEmail());
            newUser.setUsername(barber.get().getUsername());
            newUser.setPassword(barber.get().getPassword());
            newUser.setStatus(true);
            newUser.setAdmin(true);
            newUser.setBarber(true);
            LOGGER.error("Barber User Created!");
        }

        LOGGER.error("User Created Successfully!! -> User: " + newUser);
        return newUser;
    }


}
