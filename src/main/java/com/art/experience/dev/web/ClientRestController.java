package com.art.experience.dev.web;

import com.art.experience.dev.Configuration.RestCrossOriginController;
import com.art.experience.dev.model.Client;
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
    public List<Client> getClients() {
        return clientService.getClients();
    }

    @GetMapping("/{id_client}")
    @ResponseStatus(HttpStatus.OK)
    public Client getById(@PathVariable("id_client") final Long idClient) {
      LOGGER.info("Email received: \n", idClient);
        return clientService.findByID(idClient);
    }

    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Client getClientByEmail(@PathVariable("email") final String email) {
      LOGGER.info("Email received: \n", email);
        return clientService.getByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@RequestBody final Client client) {
      LOGGER.info("Email received: \n", client);
        return clientService.create(client);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client update(@RequestBody final Client client) {
      LOGGER.info("Object received: \n", client);
        return clientService.update(client);
    }

    @DeleteMapping("/logic/{id_client}")
    @ResponseStatus(HttpStatus.OK)
    public void logicDeleteByIdClient(@PathVariable("id_client") final Long client) {
      LOGGER.info("Object received: \n", client);
        clientService.logicDelete(client);
    }

    @DeleteMapping("/{id_client}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByIdClient(@PathVariable("id_client") final Long client) {
      LOGGER.info("ID received: \n", client);
        clientService.delete(client);
    }

}
