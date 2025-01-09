package com.example.danjamserver.workoutMate.repository;

import com.example.danjamserver.workoutMate.domain.PreferredWorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferredWorkoutTypeRepository extends JpaRepository<PreferredWorkoutType, Long> {
    void deleteAllByWorkoutMateProfileId(Long id);
}
