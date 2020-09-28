package com.art.experience.dev.data;

import com.art.experience.dev.model.Barber;
import com.art.experience.dev.model.BarberShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface BarberRepository extends JpaRepository<Barber, Long> {

    @Query("SELECT b FROM Barber b WHERE b.userId = :user_id")
    Optional<Barber> findByUserId(@Param("user_id") final Long userId);

    @Query("SELECT b FROM Barber b WHERE b.barberId = :barber_id")
    Optional<Barber> findById(@Param("barber_id") final Long barberId);

    @Query("SELECT b FROM Barber b WHERE b.email = :email")
    Optional<Barber> findByEmail(@Param("email") final String email);
}
