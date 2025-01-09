package com.example.danjamserver.studyMate.repository;

import com.example.danjamserver.studyMate.domain.StudyMateProfile;
import com.example.danjamserver.studyMate.enums.AverageGrade;
import com.example.danjamserver.studyMate.enums.StudyTime;
import com.example.danjamserver.studyMate.enums.StudyType;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyMateProfileRepository extends JpaRepository<StudyMateProfile, Long> {

    Optional<StudyMateProfile> findByUserId(Long userId);

    // 학교로 스터디 메이트 후보 조회
    @Query("SELECT DISTINCT sp FROM StudyMateProfile sp " +
            "JOIN sp.user u " +
            "JOIN u.school s " +
            "JOIN SchoolMajors sm ON sm.school.id = s.id AND sm.major = u.myProfile.major " +
            "JOIN sp.preferredStudyTypes pst " +
            "WHERE s.id = :schoolId " +
            "AND (:gender IS NULL OR u.gender = :gender) " +
            "AND (:minBirthYear IS NULL OR " +
            "      (CASE WHEN substring(u.myProfile.birth, 1, 2) >= '50' " +
            "            THEN CONCAT('19', substring(u.myProfile.birth, 1, 2)) " +
            "            ELSE CONCAT('20', substring(u.myProfile.birth, 1, 2)) " +
            "       END) >= :minBirthYear) " +
            "AND (:maxBirthYear IS NULL OR " +
            "      (CASE WHEN substring(u.myProfile.birth, 1, 2) >= '50' " +
            "            THEN CONCAT('19', substring(u.myProfile.birth, 1, 2)) " +
            "            ELSE CONCAT('20', substring(u.myProfile.birth, 1, 2)) " +
            "       END) <= :maxBirthYear) " +
            "AND (:minEntryYear IS NULL OR u.myProfile.entryYear >= CAST(:minEntryYear AS INTEGER)) " +
            "AND (:maxEntryYear IS NULL OR u.myProfile.entryYear <= CAST(:maxEntryYear AS INTEGER)) " +
            "AND (:colleges IS NULL OR sm.college IN :colleges) " +
            "AND (:mbti IS NULL OR FUNCTION('mbti_contains', u.myProfile.mbti, :mbti) = true) " +
            "AND (:preferredStudyTypes IS NULL OR pst.studyType IN :preferredStudyTypes)" +
            "AND (:preferredStudyTimes IS NULL OR sp.preferredStudyTime IN :preferredStudyTimes) " +
            "AND (:preferredAverageGrades IS NULL OR sp.averageGrade IN :preferredAverageGrades) "
    )
    List<StudyMateProfile> findProfilesByFilters(
            @Param("schoolId") Long schoolId,
            @Param("gender") Integer gender,
            @Param("minBirthYear") String minBirthYear,
            @Param("maxBirthYear") String maxBirthYear,
            @Param("minEntryYear") String minEntryYear,
            @Param("maxEntryYear") String maxEntryYear,
            @Param("mbti") String mbti,
            @Param("colleges") Set<String> colleges,
            @Param("preferredStudyTypes") Set<StudyType> preferredStudyTypes,
            @Param("preferredStudyTimes") Set<StudyTime> preferredStudyTimes,
            @Param("preferredAverageGrades") Set<AverageGrade> preferredAverageGrades
    );
}
