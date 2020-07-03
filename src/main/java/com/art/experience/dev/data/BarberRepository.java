package com.art.experience.dev.data;

import com.art.experience.dev.model.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface BarberRepository extends JpaRepository<Barber, Long> {
}
