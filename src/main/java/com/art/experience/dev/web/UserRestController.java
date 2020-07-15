package com.art.experience.dev.web;

import com.art.experience.dev.Configuration.RestCrossOriginController;
import com.art.experience.dev.model.User;
import com.art.experience.dev.service.BarberService;
import com.art.experience.dev.service.UserServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestCrossOriginController("/user")
public class UserRestController {

    private static final Logger LOGGER = LogManager.getLogger(UserRestController.class);
    private UserServices userServices;

    @Autowired
    public UserRestController(final UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userServices.findAllUsers();
    }

    @GetMapping("/{id_user}")
    @ResponseStatus(HttpStatus.OK)
    public User getById(@PathVariable("id_user") final Long idUser) {
        LOGGER.info("Object retreived -> \n" , idUser);
        return userServices.findUsersById(idUser);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody final User user) {
        LOGGER.info("Object retreived -> \n" , user);
        return userServices.createClientUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User update(@RequestBody final User user) {
        LOGGER.info("Object retreived -> \n" , user);
        return userServices.updateUser(user);
    }

    @DeleteMapping("/{id_user}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id_user") final Long user) {
        LOGGER.info("Object retreived -> \n" , user);
        userServices.deleteUserById(user);
    }

}
