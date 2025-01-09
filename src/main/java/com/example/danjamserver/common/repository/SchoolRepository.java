package com.example.danjamserver.common.repository;

import com.example.danjamserver.common.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {
    Optional<School> findById(Long id);

    Optional<School> findByName(String name);
}
