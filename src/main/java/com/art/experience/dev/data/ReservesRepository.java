package com.art.experience.dev.data;

import com.art.experience.dev.model.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public interface ReservesRepository extends JpaRepository<Reserve, Long> {

    @Query("SELECT s FROM Reserve s WHERE s.barberOrHairdresserId = :barberOrHairId")
    Optional<List<Reserve>> findByBarberOrHairdresserId(@Param("barberOrHairId")final Long barberOrHairId);

    @Query("SELECT s FROM Reserve s WHERE s.clientId = :clientId")
    Optional<List<Reserve>> findByClientId(@Param("clientId") final Long clientId);

    @Query("SELECT s FROM Reserve s WHERE s.barberOrHairdresserId = :barberId AND s.reserveDate <= :date")
    Optional<List<Reserve>> findByReserveIdAndBarberId(@Param("barberId") final Long barberId,
                                                       @Param("date") final LocalDate date);
}
