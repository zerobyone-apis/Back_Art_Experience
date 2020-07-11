package com.art.experience.dev.data;

import com.art.experience.dev.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUserId(final Long id);

    Optional<Client> findByEmail(final String email);
}
