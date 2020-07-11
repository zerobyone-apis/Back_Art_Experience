package com.art.experience.dev.data;

import com.art.experience.dev.model.BarberShop;
import com.art.experience.dev.model.Reserve;
import com.art.experience.dev.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface BarberShopRepository extends JpaRepository<BarberShop, Long> {

    @Query("SELECT bs FROM BarberShop bs WHERE bs.name = :name")
    Optional<List<BarberShop>> findByName(@Param("name") final String name);
}
