package com.example.danjamserver.workoutMate.repository;

import com.example.danjamserver.workoutMate.domain.WorkoutMateProfile;
import com.example.danjamserver.workoutMate.enums.WorkoutIntensity;
import com.example.danjamserver.workoutMate.enums.WorkoutTime;
import com.example.danjamserver.workoutMate.enums.WorkoutTimeZone;
import com.example.danjamserver.workoutMate.enums.WorkoutType;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkoutMateProfileRepository extends JpaRepository<WorkoutMateProfile, Long> {

    //유저로 룸메이트 프로필 조회
    Optional<WorkoutMateProfile> findByUserId(Long userId);

    // 학교로 운동메이트 후보 조회
    @Query("SELECT DISTINCT r FROM WorkoutMateProfile r " +
            "JOIN r.user u " +
            "JOIN u.school s " +
            "JOIN SchoolMajors sm ON sm.school.id = s.id AND sm.major = u.myProfile.major " +
            "JOIN r.preferredWorkoutTimes pwt " +
            "JOIN r.preferredWorkoutTimeZones pwtz " +
            "JOIN r.preferredWorkoutTypes pwty " +
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
            "AND (:preferredWorkoutTimes IS NULL OR pwt.workoutTime IN :preferredWorkoutTimes)" +
            "AND (:preferredWorkoutTimeZones IS NULL OR pwtz.workoutTimeZone IN :preferredWorkoutTimeZones) " +
            "AND (:preferredWorkoutTypes IS NULL OR pwty.workoutType = 'ANYWAY' OR pwty.workoutType IN :preferredWorkoutTypes) "
            +
            "AND (:preferredWorkoutIntensities IS NULL OR r.preferredWorkoutIntensity = 'ANYWAY' OR r.preferredWorkoutIntensity IN :preferredWorkoutIntensities) "
    )
    List<WorkoutMateProfile> findProfilesByFilters(
            @Param("schoolId") Long schoolId,
            @Param("gender") Integer gender,
            @Param("minBirthYear") String minBirthYear,
            @Param("maxBirthYear") String maxBirthYear,
            @Param("minEntryYear") String minEntryYear,
            @Param("maxEntryYear") String maxEntryYear,
            @Param("mbti") String mbti,
            @Param("colleges") Set<String> colleges,
            @Param("preferredWorkoutTimeZones") Set<WorkoutTimeZone> preferredWorkoutTimeZones,
            @Param("preferredWorkoutIntensities") Set<WorkoutIntensity> preferredWorkoutIntensities,
            @Param("preferredWorkoutTimes") Set<WorkoutTime> preferredWorkoutTimes,
            @Param("preferredWorkoutTypes") Set<WorkoutType> preferredWorkoutTypes
    );


}
