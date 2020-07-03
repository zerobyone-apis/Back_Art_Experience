package com.art.experience.dev.data;

import com.art.experience.dev.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Work,Long> {

}
