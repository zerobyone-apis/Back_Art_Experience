package com.art.experience.dev.service;

import com.art.experience.dev.data.ClientRepository;
import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.exception.CreateResourceException;
import com.art.experience.dev.model.Client;
import com.art.experience.dev.model.User;
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
public class ClientService {

    private static final Logger LOGGER = LogManager.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Autowired
    private ClientService(final ClientRepository clientRepository,
                          final UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }


    public Client getClientByEmail(final String email) {
        Optional<Client> client = clientRepository.findByEmail(email);
         if (!client.isPresent()) {
            LOGGER.error("Client Email: [ "+ email + " ] Not found.\n Please try to create new Client Account to create new Reserve. :) ");
            throw new ResourceNotFoundException("Client Email: [ "+ email + " ] Not found.\n Please try to create new Client Account to create new Reserve. :) ");
        }
        return client.get();
    }

    public Client findByID(final Long clientId) {
        Optional<Client> client = clientRepository.findById(clientId);
         if (!client.isPresent()) {
            LOGGER.error("Client ID: [ "+ clientId +" ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
            throw new ResourceNotFoundException("Client ID: [ "+ clientId +" ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
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
            User user = createUser(client);
            newClient.setUserId(user.getUserId());

            return clientRepository.save(newClient);
        } catch (Exception e) {
            LOGGER.error("Something failed on the creation of Client. " + e.getMessage());
            throw new IllegalArgumentException("Something failed on the creation of Client. " + e.getMessage());
        }
    }

    public Client update(final Client clie){
        Optional<Client> client = clientRepository.findById(clie.getClientId());
        if(!client.isPresent()){
            LOGGER.error("Client ID: [ "+ clie.getClientId()+" ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
            throw new ResourceNotFoundException("Client ID: [ "+ clie.getClientId() +" ] Not found. \n Please try to create new Client Account to create new Reserve. :)");
        }
        // My new Client Update object
        Client updatedClient = client.get();

        /* Immutable client Info
        *
        * updatedClient.setUserId(client.get().getUserId());
        * updatedClient.setClientId(client.get().getClientId());
        * updatedClient.setStartDate(client.get().getStartDate());
        *
        */

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

        if(Objects.nonNull(clie.getEndDate())){
            updatedClient.setEndDate(Instant.now());
            updatedClient.setStatus(false);
        }
        updateUser(updatedClient);
        return clientRepository.save(updatedClient);
    }

    public void logicDelete(final Long clientID){
        Optional<Client> client = clientRepository.findById(clientID);
        if (!client.isPresent()){
            LOGGER.error("Client not Found by this ID" + clientID);
            throw new ResourceNotFoundException("Client not Found by this ID" + clientID);
        }else{
            // Borrado logico
            client.get().setStatus(false);
            clientRepository.save(client.get());
        }
    }

    public void delete(final Long clientID){
        Optional<Client> client = clientRepository.findById(clientID);

        if (!client.isPresent()){
            LOGGER.error("Client not Found by this ID" + clientID);
            throw new ResourceNotFoundException("Client not Found by this ID" + clientID);
        }else{
            Optional<User> user = userRepository.findById(client.get().getUserId());
            // Se borra el usuario tambien.
            userRepository.delete(user.get());
            // Se dejara un boton para los admins que podran eliminar clientes.
            clientRepository.delete(client.get());
        }
    }

    private User updateUser(final Client updateClient) {
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
            user.setUsername(client.getUsername());
            user.setPassword(client.getPassword());
            user.setCreateOn(Instant.now());
            user.setStatus(true);
            return userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error("Error creating the user. " + e.getMessage());
            throw new CreateResourceException("Error creating the user. " + e.getMessage());
        }
    }
}
