package com.art.experience.dev.web;

import com.art.experience.dev.Configuration.RestCrossOriginController;
import com.art.experience.dev.model.Client;
import com.art.experience.dev.model.DTO.DTOClientResponse;
import com.art.experience.dev.service.ClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestCrossOriginController("/client")
public class ClientRestController {

    private static final Logger LOGGER = LogManager.getLogger(ClientRestController.class);
    private ClientService clientService;

    @Autowired
    public ClientRestController(final ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<DTOClientResponse> getClients() {
        LOGGER.info("Getting Clients. . . (> ° ^ °)> ");
        return clientService.getClients();
    }

    @GetMapping("/{id_client}")
    @ResponseStatus(HttpStatus.OK)
    public DTOClientResponse getById(@PathVariable("id_client") final Long idClient) {
        LOGGER.info("ID Client received: \n", idClient);
        return clientService.findByID(idClient);
    }

    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public DTOClientResponse getClientByEmail(@PathVariable("email") final String email) {
      LOGGER.info("Email received: \n", email);
        return clientService.getByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DTOClientResponse create(@RequestBody final Client client) {
        LOGGER.info("Client received: \n", "username: " + client.getUsername() + ", email: " + client.getEmail());
        return clientService.create(client);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DTOClientResponse update(@RequestBody final Client client) {
      LOGGER.info("Client received: \n", "username: " + client.getUsername() + ", email: " + client.getEmail());
        return clientService.update(client);
    }

    @DeleteMapping("/logic/{id_client}")
    @ResponseStatus(HttpStatus.OK)
    public void logicDeleteByIdClient(@PathVariable("id_client") final Long client) {
        LOGGER.info("ID Client received: \n", "Client ID: " + client);
        clientService.logicDelete(client);
    }

    @DeleteMapping("/{id_client}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByIdClient(@PathVariable("id_client") final Long client) {
        LOGGER.info("ID Client received: \n", "Client ID: " + client);
       clientService.delete(client);
    }

}
