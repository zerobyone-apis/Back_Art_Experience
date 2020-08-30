package com.art.experience.dev.service;

import com.art.experience.dev.data.ClientRepository;
import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.exception.CreateResourceException;
import com.art.experience.dev.model.Client;
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

    public Client getByEmail(final String email) {
        Optional<Client> client = clientRepository.findByEmail(email);
        if (!client.isPresent()) {
            LOGGER.error("Client Email: [ " + email + " ] Not found.\n Please try to create new Client Account to create new Reserve. :) ");
            throw new ResourceNotFoundException("Client Email: [ " + email + " ] Not found.\n Please try to create new Client Account to create new Reserve. :) ");
        }
        return client.get();
    }

    public Client findByUserId(final Long userId) {
        Optional<Client> client = clientRepository.findByUserId(userId);
        if (!client.isPresent()) {
            LOGGER.error("Client User ID: [ " + userId + " ] Not found.\n Please try to create new Client Account to create new Reserve. :) ");
            throw new ResourceNotFoundException("Client User ID: [ " + userId + " ] Not found.\n Please try to create new Client Account to create new Reserve. :) ");
        }
        return client.get();
    }

    public Client findByID(final Long clientId) {
        Optional<Client> client = clientRepository.findById(clientId);
        if (!client.isPresent()) {
            LOGGER.error("Client ID: [ " + clientId + " ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
            throw new ResourceNotFoundException("Client ID: [ " + clientId + " ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
        }
        return client.get();
    }

    public List<Client> getClients() {
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            LOGGER.error("Clients not found in the database");
            throw new ResourceNotFoundException("No Clients on the Database :c");
        }
        return clients.stream()
                .map(client -> (Client) client)
                .collect(Collectors.toList());
    }

    public Client create(final Client client) {
        Client newClient = new Client();

        LOGGER.error("Start Username Validation");
        Optional<User> usernameValidation = userRepository.findByUsername(client.getUsername());
    if (!usernameValidation.isPresent()) {
            LOGGER.error(client.getUsername() + " already exists, please try with another Username.");
            throw new CreateResourceException(client.getUsername() + " already exists, please try with another Username.");
        }

        try {
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
            newClient.setStatus(true);

            // User Information
            User user = createGenericUser(Optional.empty(), Optional.of(client), Optional.empty());
            newClient.setUserId(user.getUserId());

            return clientRepository.save(newClient);
        } catch (Exception e) {
            LOGGER.error("Error creating this client: " + e.getMessage());
            throw new CreateResourceException("Error creating this client: " + e.getMessage());
        }
    }

    public Client update(final Client clie) {
        LOGGER.info("Start ID Validation");
        Optional<Client> client = clientRepository.findById(clie.getClientId());
        if (!client.isPresent()) {
            LOGGER.error("ID Validation Error: ");
            LOGGER.error("Client ID: [ " + clie.getClientId() + " ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
            throw new ResourceNotFoundException("Client ID: [ " + clie.getClientId() + " ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
        }
        LOGGER.info("Finish ID Validation Success!");

        LOGGER.info("Start Username Validation");
        Optional<User> usernameValidation = userRepository.findByUsername(clie.getUsername());
        if (!usernameValidation.isEmpty()) {
            LOGGER.error("Username Validation Error: ");
            LOGGER.error(clie.getUsername() + " already exists, please try with another Username.");
            throw new CreateResourceException(clie.getUsername() + " already exists, please try with another Username.");
        }
        LOGGER.info("Finish Username Validation Success!");

        // My new Client Update object
        Client updatedClient = client.get();

        /* Immutable client Info
         *
         * updatedClient.setUserId(client.get().getUserId());
         * updatedClient.setClientId(client.get().getClientId());
         * updatedClient.setStartDate(client.get().getStartDate());
         *
         */
        try {
            // Client Information
            updatedClient.setUsername(clie.getUsername());
            updatedClient.setPassword(clie.getPassword());
            updatedClient.setName(clie.getName());
            updatedClient.setEmail(clie.getEmail());
            updatedClient.setCel(clie.getCel());

            // Analytics Info
            updatedClient.setInteractions(Objects.isNull(clie.getInteractions()) ? updatedClient.getInteractions() : clie.getInteractions());
            updatedClient.setAmountReserves(Objects.isNull(clie.getAmountReserves()) ? updatedClient.getAmountReserves() : clie.getAmountReserves());
            updatedClient.setClientType(Objects.isNull(clie.getClientType()) ? updatedClient.getClientType() : clie.getClientType());
            updatedClient.setLastDateUpdated(Instant.now());
            updatedClient.setStatus(true);

            if (Objects.nonNull(clie.getEndDate())) {
                updatedClient.setEndDate(Instant.now());
                updatedClient.setStatus(false);
            }

            updateGenericUser(Optional.empty(), Optional.of(updatedClient), Optional.empty(), Optional.of(client.get().getUserId()));
            return clientRepository.save(updatedClient);
        } catch (Exception ex) {
            LOGGER.error("Error updating this client: " + ex.getMessage());
            throw new CreateResourceException("Error updating this client: " + ex.getMessage());
        }
    }

    public void logicDelete(final Long clientID) {
        Optional<Client> client = clientRepository.findById(clientID);
        if (!client.isPresent()) {
            LOGGER.error("Client not Found by this ID" + clientID);
            throw new ResourceNotFoundException("Client not Found by this ID" + clientID);
        } else {
            // Borrado logico
            try {
                client.get().setStatus(false);
                clientRepository.save(client.get());
            } catch (Exception e) {
                LOGGER.error("Something went wrong deleting Client with this ID " + clientID + ", Error message: " + e.getMessage());
                throw new ResourceNotFoundException("Something went wrong deleting Client with this ID " + clientID + ", Error message: " + e.getMessage());
            }

        }
    }

    public void delete(final Long clientID) {
        Optional<Client> client = clientRepository.findById(clientID);

        if (!client.isPresent()) {
            LOGGER.error("Client not Found by this ID" + clientID);
            throw new ResourceNotFoundException("Client not Found by this ID" + clientID);
        } else {
            Optional<User> user = userRepository.findById(client.get().getUserId());
            // Se borra el usuario tambien.
            userRepository.delete(user.get());
            // Se dejara un boton para los admins que podran eliminar clientes.
            clientRepository.delete(client.get());
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

            *//****** User Information ********//*

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
