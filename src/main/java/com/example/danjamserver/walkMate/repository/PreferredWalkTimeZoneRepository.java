package com.example.danjamserver.walkMate.repository;

import com.example.danjamserver.walkMate.domain.PreferredWalkTimeZone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferredWalkTimeZoneRepository extends JpaRepository<PreferredWalkTimeZone, Long> {
    void deleteAllByWalkMateProfileId(Long id);
}
