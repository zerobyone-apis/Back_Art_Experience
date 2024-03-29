package com.art.experience.dev.service;

import com.art.experience.dev.data.ClientRepository;
import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.exception.CreateResourceException;
import com.art.experience.dev.model.Client;
import com.art.experience.dev.model.DTO.DTOClientResponse;
import com.art.experience.dev.model.User;
import com.art.experience.dev.service.abstractions.UserAbstractFunctions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService extends UserAbstractFunctions {

    private static final Logger LOGGER = LogManager.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Autowired
    private ClientService(final ClientRepository clientRepository,
                          final UserRepository userRepository) {
        super(userRepository);
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    public DTOClientResponse getByEmail(final String email) {
        Optional<Client> client = clientRepository.findFirstByEmail(email);
        if (client.isPresent()) {
            return decoratorPatternClient(client.get());
        }
        LOGGER.error("Client Email: [ " + email + " ] Not found.\n Please try to create new Client Account to create new Reserve. :) ");
        throw new ResourceNotFoundException("Client Email: [ " + email + " ] Not found.\n Please try to create new Client Account to create new Reserve. :) ");
    }

    public DTOClientResponse findByUserId(final Long userId) {
        Optional<Client> client = clientRepository.findByUserId(userId);
        if (!client.isPresent()) {
            LOGGER.error("Client User ID: [ " + userId + " ] Not found.\n Please try to create new Client Account to create new Reserve. :) ");
            throw new ResourceNotFoundException("Client User ID: [ " + userId + " ] Not found.\n Please try to create new Client Account to create new Reserve. :) ");
        }
        return decoratorPatternClient(client.get());
    }

    public DTOClientResponse findByID(final Long clientId) {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isPresent()) {
            return decoratorPatternClient(client.get());
        }
        LOGGER.error("Client ID: [ " + clientId + " ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
        throw new ResourceNotFoundException("Client ID: [ " + clientId + " ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
    }

    public List<DTOClientResponse> getClients() {
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            LOGGER.error("Clients not found in the database");
            throw new ResourceNotFoundException("No Clients on the Database :c");
        }
        return clients.stream()
                .map(this::decoratorPatternClient)
                .collect(Collectors.toList());
    }

    public DTOClientResponse create(final Client client) {
        Client newClient = new Client();
        try {
            LOGGER.info("Username & Email Validation");
            Optional<User> userCheck = userRepository.findByUsernameOrEmail(client.getUsername(),client.getEmail());

            if (userCheck.isPresent()) {
                LOGGER.debug("Username or Email already exists, please try to sign up or enter another Username or Email ");
                throw new CreateResourceException("Username or Email already exists, please try to sign up or enter another Username or Email ");
            }

            // Client information
            newClient.setName(client.getName());
            newClient.setUsername(client.getUsername());
            newClient.setPassword(client.getPassword());
            newClient.setEmail(client.getEmail());
            newClient.setCel(client.getCel());

            // Analytics info
            newClient.setInteractions(Objects.isNull(client.getInteractions()) ? "None Interactions" : client.getInteractions());
            newClient.setAmountReserves(Objects.isNull(client.getAmountReserves()) ? 0L : client.getAmountReserves());
            newClient.setClientType(Objects.isNull(client.getClientType()) ? "Basic client" : client.getClientType());
            newClient.setStartDate(Instant.now());
            newClient.setSocialNumber(Objects.nonNull(client.getSocialNumber()) ? client.getSocialNumber() : null);
            newClient.setStatus(true);

            // User Information
            User user = createGenericUser(Optional.empty(), Optional.of(client), Optional.empty());
            newClient.setUserId(user.getUserId());
            newClient.setSocialNumber(user.getSocialNumber());

            return decoratorPatternClient(clientRepository.save(newClient));
        } catch (Exception e) {
            LOGGER.debug("Error creating this client: " + e.getMessage());
            throw new CreateResourceException("Error creating this client: " + e.getMessage());
        }
    } //todo migrated

    public DTOClientResponse update(final Client clie) {
        LOGGER.info("Start ID Validation");
        Client client = clientRepository.findById(clie.getClientId()).get();
        if (Objects.isNull(client)) {
            LOGGER.debug("ID Validation Error: ");
            LOGGER.debug("Client ID: [ " + clie.getClientId() + " ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
            throw new ResourceNotFoundException("Client ID: [ " + clie.getClientId() + " ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
        }
        LOGGER.info("Finish ID Validation Success!");

        LOGGER.info("Start Username Validation");
        Optional<User> username = userRepository.findByUsername(clie.getUsername());
        if (username.isPresent()) {
            LOGGER.debug("Username Validation Error: ");
            LOGGER.debug(clie.getUsername() + " already exists, please try with another Username.");
            throw new CreateResourceException(clie.getUsername() + " already exists, please try with another Username.");
        } else {
            LOGGER.info("Finish Username Validation Success!");

            /* Immutable client Info
             *
             * updatedClient.setUserId(client.get().getUserId());
             * updatedClient.setClientId(client.get().getClientId());
             * updatedClient.setStartDate(client.get().getStartDate());
             *
             */
            try {
                // Client Information
                client.setUsername(clie.getUsername());
                client.setPassword(clie.getPassword());
                client.setName(clie.getName());
                client.setEmail(clie.getEmail());
                client.setCel(clie.getCel());

                // Analytics Info
                client.setInteractions(Objects.isNull(clie.getInteractions()) ? client.getInteractions() : clie.getInteractions());
                client.setAmountReserves(Objects.isNull(clie.getAmountReserves()) ? client.getAmountReserves() : clie.getAmountReserves());
                client.setClientType(Objects.isNull(clie.getClientType()) ? client.getClientType() : clie.getClientType());
                client.setLastDateUpdated(Instant.now());
                client.setStatus(true);

                if (Objects.nonNull(clie.getEndDate())) {
                    client.setEndDate(Instant.now());
                    client.setStatus(false);
                }

                updateGenericUser(Optional.empty(), Optional.of(client), Optional.empty(), Optional.of(client.getUserId()));
                return decoratorPatternClient(clientRepository.save(client));
            } catch (Exception ex) {
                LOGGER.debug("Error updating this client: " + ex.getMessage());
                throw new CreateResourceException("Error updating this client: " + ex.getMessage());
            }
        }
    }

    public void logicDelete(final Long clientID) {
        Optional<Client> client = clientRepository.findById(clientID);
        if (client.isPresent()) {
            // Borrado logico
            try {
                client.get().setStatus(false);
                clientRepository.save(client.get());
            } catch (Exception e) {
                LOGGER.error("Something went wrong deleting Client with this ID " + clientID + ", Error message: " + e.getMessage());
                throw new ResourceNotFoundException("Something went wrong deleting Client with this ID " + clientID + ", Error message: " + e.getMessage());
            }

        } else {
            LOGGER.error("Client not Found by this ID" + clientID);
            throw new ResourceNotFoundException("Client not Found by this ID" + clientID);
        }
    }

    public void delete(final Long clientID) {
        Optional<Client> client = clientRepository.findById(clientID);
        if (client.isPresent()) {
            Optional<User> user = userRepository.findById(client.get().getUserId());
            // Se borra el usuario tambien.
            userRepository.delete(user.get());
            // Se dejara un boton para los admins que podran eliminar clientes.
            clientRepository.delete(client.get());
        } else {
            LOGGER.error("Client not Found by this ID" + clientID);
            throw new ResourceNotFoundException("Client not Found by this ID" + clientID);
        }
    }

 /*   private User updateUser(final Client updateClient) {
        Optional<User> user = userRepository.findById(updateClient.getUserId());
        if(!user.isPresent()){
            LOGGER.error("User not Found");
            throw new ResourceNotFoundException("User not Found with this ID " + updateClient.getUserId());
        }
        user.get().setUsername(updateClient.getUsername());
        user.get().setPassword(updateClient.getPassword());
        if(Objects.nonNull(updateClient.getEndDate())){
            user.get().setDeleteOn(Instant.now());
            user.get().setStatus(false);
        }
        return userRepository.saveAndFlush(user.get());
    }

    private User createUser(final Client client) {
        User user = new User();
        try {
            Optional<User> checkUser = userRepository.findByUsername(client.getUsername());
            if(!checkUser.isEmpty()){
                LOGGER.error(client.getUsername() + " already exists, please try with another Username.");
                throw new CreateResourceException(client.getUsername() + " already exists, please try with another Username.");
            }

            /****** User Information *******

            user.setUsername(client.getUsername());
            user.setPassword(client.getPassword());
            user.setCreateOn(Instant.now());
            user.setStatus(true);
            return userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error("Error creating the user. " + e.getMessage());
            throw new CreateResourceException("Error creating the user. " + e.getMessage());
        }
    }*/
}
