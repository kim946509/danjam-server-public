package com.example.danjamserver.workoutMate.repository;

import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTimeZone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferredWorkoutTimeZoneRepository extends JpaRepository<PreferredWorkoutTimeZone, Long> {
    void deleteAllByWorkoutMateProfileId(Long id);
}
