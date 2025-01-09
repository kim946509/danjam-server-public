package com.example.danjamserver.foodMate.repository;

import com.example.danjamserver.foodMate.domain.MealMateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealMateTimeRepository extends JpaRepository<MealMateTime, Long> {
    void deleteAllByFoodMateProfileId(Long id);
}
