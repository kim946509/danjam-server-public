package com.example.danjamserver.roomMate.repository;

import com.example.danjamserver.roomMate.domain.OwnSleepHabit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnSleepHabitRepository extends JpaRepository<OwnSleepHabit, Long> {
    void deleteAllByRoomMateProfileId(Long id);
}
