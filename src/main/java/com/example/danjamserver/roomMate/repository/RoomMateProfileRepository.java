package com.example.danjamserver.roomMate.repository;

import com.example.danjamserver.roomMate.domain.RoomMateProfile;
import com.example.danjamserver.roomMate.enums.ActivityTime;
import com.example.danjamserver.roomMate.enums.CleanPeriod;
import com.example.danjamserver.roomMate.enums.Level;
import com.example.danjamserver.roomMate.enums.ShowerTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomMateProfileRepository extends JpaRepository<RoomMateProfile, Long> {

    //유저로 룸메이트 프로필 조회
    Optional<RoomMateProfile> findByUserId(Long userId);

    // 학교와 성별로 룸메이트 후보 조회
    @Query("SELECT DISTINCT r FROM RoomMateProfile r " +
            "JOIN r.user u " +
            "JOIN u.school s " +
            "JOIN SchoolMajors sm ON sm.school.id = s.id AND sm.major = u.myProfile.major " +
            "JOIN r.hopeDormitories hd " +
            "JOIN r.hopeRoomPersons hrp " +
            "WHERE s.id = :schoolId " +
            "AND u.gender = :gender " +
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
            "AND (:isSmoking IS NULL OR r.isSmoking = :isSmoking) " +
            "AND (:hotLevel IS NULL OR r.hotLevel = 'ANYWAY' OR r.hotLevel = :hotLevel) " +
            "AND (:coldLevel IS NULL OR r.coldLevel = 'ANYWAY' OR r.coldLevel = :coldLevel) " +
            "AND (:activityTime IS NULL OR r.activityTime = 'ANYWAY' OR r.activityTime = :activityTime) " +
            "AND (:cleanPeriod IS NULL OR r.cleanPeriod = 'ANYWAY' OR r.cleanPeriod = :cleanPeriod) " +
            "AND (:sleepHabits IS NULL OR " +
            "      (:sleepHabits = 1 AND SIZE(r.ownSleepHabits) = 0) " +
            "      OR (:sleepHabits = 2 AND SIZE(r.ownSleepHabits) <= 2) " +
            "      OR (:sleepHabits = 3 AND SIZE(r.ownSleepHabits) <= 4) " +
            "      OR (:sleepHabits = 4)) " +
            "AND (:showerTime IS NULL OR " +
            "       r.showerTime = :showerTime OR " +
            "       (:showerTime = com.example.danjamserver.roomMate.enums.ShowerTime.TWENTY_TO_THIRTY AND r.showerTime IN (com.example.danjamserver.roomMate.enums.ShowerTime.TEN_TO_TWENTY, com.example.danjamserver.roomMate.enums.ShowerTime.TWENTY_TO_THIRTY)) OR "
            +
            "       (:showerTime = com.example.danjamserver.roomMate.enums.ShowerTime.THIRTY_TO_FORTY AND r.showerTime IN (com.example.danjamserver.roomMate.enums.ShowerTime.TEN_TO_TWENTY, com.example.danjamserver.roomMate.enums.ShowerTime.TWENTY_TO_THIRTY, com.example.danjamserver.roomMate.enums.ShowerTime.THIRTY_TO_FORTY)) OR "
            +
            "       (:showerTime = com.example.danjamserver.roomMate.enums.ShowerTime.FORTY_OVER)) " +
            "AND (:hopeDormitories IS NULL OR hd.hopeDormitory IN :hopeDormitories) " +
            "AND (:mbti IS NULL OR FUNCTION('mbti_contains', u.myProfile.mbti, :mbti) = true) " +
            "AND (:hopeRoomPersons IS NULL OR hrp.hopeRoomPerson IN :hopeRoomPersons) "
//            "ORDER BY FUNCTION('RAND') " // MariaDB의 랜덤 정렬 함수
    )
    List<RoomMateProfile> findProfilesByFilters(
            @Param("schoolId") Long schoolId,
            @Param("mbti") String mbti,
            @Param("gender") Integer gender,
            @Param("minBirthYear") String minBirthYear,
            @Param("maxBirthYear") String maxBirthYear,
            @Param("minEntryYear") String minEntryYear,
            @Param("maxEntryYear") String maxEntryYear,
            @Param("colleges") Set<String> colleges,
            @Param("isSmoking") Boolean isSmoking,
            @Param("hotLevel") Level hotLevel,
            @Param("coldLevel") Level coldLevel,
            @Param("activityTime") ActivityTime activityTime,
            @Param("cleanPeriod") CleanPeriod cleanPeriod,
            @Param("sleepHabits") Integer sleepHabits,
            @Param("showerTime") ShowerTime showerTime,
            @Param("hopeDormitories") Set<String> hopeDormitories,
            @Param("hopeRoomPersons") Set<Integer> hopeRoomPersons
    );
}
