package com.art.experience.dev.service;

import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.model.Barber;
import com.art.experience.dev.model.Client;
import com.art.experience.dev.model.DTOUserLogin;
import com.art.experience.dev.model.User;
import com.art.experience.dev.service.abstractions.UserAbstractFunctions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends UserAbstractFunctions {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private final UserRepository repository;

    @Autowired
    public UserService(final UserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public User findUsersById(final Long id) {
        return repository.findById(id).get();
    }

    public DTOUserLogin login(final DTOUserLogin user) {
        DTOUserLogin dtoUserLoaded = loginUser(user);
        return dtoUserLoaded;
    }

    public User createUser(final Optional<User> user, final Optional<Client> client, final Optional<Barber> barber) {
        return createGenericUser(user,client,barber);
    }

    public User updateUser(final Optional<User> user, final Optional<Client> client, final Optional<Barber> barber) {
       return updateGenericUser(user,client,barber,Optional.empty());
    }

    public void deleteUserById(Long idUser) {
        Optional<User> user = repository.findById(idUser);
        user.get().setStatus(false);
        repository.save(user.get());
    }

}
