package com.art.experience.dev.web;

import com.art.experience.dev.Configuration.RestCrossOriginController;
import com.art.experience.dev.model.DTOUserLogin;
import com.art.experience.dev.model.User;
import com.art.experience.dev.service.UserService;
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
    private UserService userServices;

    @Autowired
    public UserRestController(final UserService userServices) {
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
        LOGGER.info("ID User received -> \n" , idUser);
        return userServices.findUsersById(idUser);
    }

    @PostMapping("/v1/login")
    @ResponseStatus(HttpStatus.OK)
    public DTOUserLogin login(@RequestBody final DTOUserLogin login) {
        LOGGER.info("DTO Received: \n", login.getEmail() + " | Password: "+ login.getPassword() + " | Social Number: "+ login.getSocialNumber());
        return userServices.login(login);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody final User user) {
        LOGGER.info("User received -> \n" , user.getUsername() +" | Email: " + user.getEmail());
        return userServices.createUser( Optional.of(user), Optional.empty(), Optional.empty());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User update(@RequestBody final User user) {
        LOGGER.info("User received -> \n" , user.getUsername() +" | Email: " + user.getEmail());

        return userServices.updateUser(Optional.of(user), Optional.empty(), Optional.empty());
    }

    @DeleteMapping("/{id_user}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id_user") final Long user) {
        LOGGER.info("Id user received -> \n" , user);
        userServices.deleteUserById(user);
    }

}
