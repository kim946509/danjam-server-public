package com.example.danjamserver.foodMate.repository;

import com.example.danjamserver.foodMate.domain.HopeMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HopeMenuRepository extends JpaRepository<HopeMenu, Long> {
    void deleteAllByFoodMateProfileId(Long id);
}
