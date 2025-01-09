package com.example.danjamserver.roomMate.repository;

import com.example.danjamserver.roomMate.domain.HopeRoomPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HopeRoomPersonRepository extends JpaRepository<HopeRoomPerson, Long> {
    void deleteAllByRoomMateProfileId(Long id);
}
