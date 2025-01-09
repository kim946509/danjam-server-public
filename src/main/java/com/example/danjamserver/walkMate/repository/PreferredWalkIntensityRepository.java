package com.example.danjamserver.walkMate.repository;

import com.example.danjamserver.walkMate.domain.PreferredWalkIntensity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferredWalkIntensityRepository extends JpaRepository<PreferredWalkIntensity, Long> {
    void deleteAllByWalkMateProfileId(Long id);
}
