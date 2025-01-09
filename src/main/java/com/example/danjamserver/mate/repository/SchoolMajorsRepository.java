package com.example.danjamserver.mate.repository;

import com.example.danjamserver.mate.domain.SchoolMajors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchoolMajorsRepository extends JpaRepository<SchoolMajors, Long> {

    //schoolId와 major로 단과대명 가져오기
    Optional<SchoolMajors> findBySchoolIdAndMajor(Long schoolId, String major);

    //schoolId로 전공 리스트 가져오기
    List<SchoolMajors> findBySchoolId(Long schoolId);
}
