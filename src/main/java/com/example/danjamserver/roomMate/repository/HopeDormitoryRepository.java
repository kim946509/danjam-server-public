package com.example.danjamserver.roomMate.repository;

import com.example.danjamserver.roomMate.domain.HopeDormitory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HopeDormitoryRepository extends JpaRepository<HopeDormitory, Long> {
    void deleteAllByRoomMateProfileId(Long id);
}
