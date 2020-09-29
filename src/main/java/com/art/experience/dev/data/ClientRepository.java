package com.art.experience.dev.data;

import com.art.experience.dev.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUserId(final Long id);

    @Query(value = "SELECT * FROM Client c WHERE c.email = :email ORDER BY DESC LIMIT 1", nativeQuery = true)
    Optional<Client> findFirstByEmail(@Param("email") final String email);
}
