package com.example.danjamserver.walkMate.repository;

import com.example.danjamserver.walkMate.domain.WalkMateProfile;
import com.example.danjamserver.walkMate.enums.WalkIntensity;
import com.example.danjamserver.walkMate.enums.WalkTime;
import com.example.danjamserver.walkMate.enums.WalkTimeZone;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalkMateProfileRepository extends JpaRepository<WalkMateProfile, Long> {
    Optional<WalkMateProfile> findByUserId(Long id);

    // 학교로 산책 메이트 후보 조회
    @Query("SELECT DISTINCT r FROM WalkMateProfile r " +
            "JOIN r.user u " +
            "JOIN u.school s " +
            "JOIN SchoolMajors sm ON sm.school.id = s.id AND sm.major = u.myProfile.major " +
            "JOIN r.preferredWalkTimeZones pwtz " +
            "JOIN r.preferredWalkIntensities pwi " +
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
            "AND (:preferredWalkTimes IS NULL OR r.preferredWalkTime IN :preferredWalkTimes)" +
            "AND (:preferredWalkTimeZones IS NULL OR pwtz.walkTimeZone IN :preferredWalkTimeZones) " +
            "AND (:preferredWalkIntensities IS NULL OR pwi.walkIntensity IN :preferredWalkIntensities) "
    )
    List<WalkMateProfile> findProfilesByFilters(
            @Param("schoolId") Long schoolId,
            @Param("gender") Integer gender,
            @Param("minBirthYear") String minBirthYear,
            @Param("maxBirthYear") String maxBirthYear,
            @Param("minEntryYear") String minEntryYear,
            @Param("maxEntryYear") String maxEntryYear,
            @Param("mbti") String mbti,
            @Param("preferredWalkTimes") Set<WalkTime> preferredWalkTimes,
            @Param("colleges") Set<String> colleges,
            @Param("preferredWalkTimeZones") Set<WalkTimeZone> preferredWalkTimeZones,
            @Param("preferredWalkIntensities") Set<WalkIntensity> preferredWalkIntensities
    );

}
