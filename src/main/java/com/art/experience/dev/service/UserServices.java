package com.art.experience.dev.service;

import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.exception.CreateResourceException;
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

@Service
public class UserServices {

    private static final Logger LOGGER = LogManager.getLogger(ClientService.class);
    private final UserRepository repository;

    @Autowired
    public UserServices( final UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public User findUsersById(final Long id) {
        return repository.findById(id).get();
    }

    public User createClientUser(final User bodyUser) {
        User user = new User();
        try {
            Optional<User> checkUser = repository.findByUsername(bodyUser.getUsername());
            if(!checkUser.isEmpty()){
                LOGGER.error(bodyUser.getUsername() + " already exists, please try with another Username.");
                throw new CreateResourceException(bodyUser.getUsername() + " already exists, please try with another Username.");
            }

            user.setUsername(bodyUser.getUsername());
            user.setPassword(bodyUser.getPassword());
            user.setCreateOn(Instant.now());
            user.setStatus(true);
        }catch(Exception ex){
            LOGGER.error("Something failed on the creation of Client. " + ex.getMessage());
            throw new IllegalArgumentException("Something failed on the creation of Client. " + ex.getMessage());
        }
        return repository.save(user);
    }

    public User updateUser(final User bodyUser) {
        Optional<User> user = repository.findById(bodyUser.getUserId());
        if(!user.isPresent()){
            LOGGER.error("Barber not found with the ID " + user.get().getUserId());
            throw new ResourceNotFoundException("Barber not found with the ID " + user.get().getUserId());
        }
        try{
            user.get().setUsername(bodyUser.getUsername());
            user.get().setPassword(bodyUser.getPassword());
            if(Objects.nonNull(bodyUser.getDeleteOn())){
                user.get().setDeleteOn(Instant.now());
                user.get().setStatus(false);
            }
        }catch(Exception ex){
            LOGGER.error("Something failed on the creation of Client. " + ex.getMessage());
            throw new CreateResourceException("Something failed on the creation of user. " + ex.getMessage());
        }
        return repository.save(user.get());
    }

    public void deleteUserById(Long idUser) {
        Optional<User> user = repository.findById(idUser);
        user.get().setStatus(false);
        repository.save(user.get());
    }

}
