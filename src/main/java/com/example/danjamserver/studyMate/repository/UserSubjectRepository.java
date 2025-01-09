package com.example.danjamserver.studyMate.repository;

import com.example.danjamserver.studyMate.domain.UserSubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubjectRepository extends JpaRepository<UserSubject, Long> {
    void deleteAllByStudyMateProfileId(Long id);
}
