package com.example.danjamserver.workoutMate.repository;

import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferredWorkoutTimeRepository extends JpaRepository<PreferredWorkoutTime, Long> {
    void deleteAllByWorkoutMateProfileId(Long id);
}
