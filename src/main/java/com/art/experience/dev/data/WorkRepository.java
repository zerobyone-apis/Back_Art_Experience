package com.art.experience.dev.data;

import com.art.experience.dev.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface WorkRepository extends JpaRepository<Work, Long> {

    @Query("SELECT w FROM Work w WHERE w.workToDo = :typeWork")
    Optional<Work> findByWorkToDo(final String typeWork);
}
