package com.example.danjamserver.studyMate.repository;

import com.example.danjamserver.studyMate.domain.PreferredStudyType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferredStudyTypeRepository extends JpaRepository<PreferredStudyType, Long> {
    void deleteAllByStudyMateProfileId(Long id);
}
