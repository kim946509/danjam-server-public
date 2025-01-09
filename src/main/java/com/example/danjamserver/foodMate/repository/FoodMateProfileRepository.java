package com.example.danjamserver.foodMate.repository;

import com.example.danjamserver.foodMate.domain.FoodMateProfile;
import com.example.danjamserver.foodMate.enums.DiningManner;
import com.example.danjamserver.foodMate.enums.MateTime;
import com.example.danjamserver.foodMate.enums.MealTime;
import com.example.danjamserver.foodMate.enums.Menu;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FoodMateProfileRepository extends JpaRepository<FoodMateProfile, Long> {
    //유저로 푸드메이트 프로필 조회
    Optional<FoodMateProfile> findByUserId(Long userId);

    // 학교와 성별로 식사메이트 후보 조회
    @Query("SELECT DISTINCT fp FROM FoodMateProfile fp " +
            "JOIN fp.user u " +
            "JOIN u.school s " +
            "JOIN SchoolMajors sm ON sm.school.id = s.id AND sm.major = u.myProfile.major " +
            "JOIN fp.mateTime mateTime " +
            "JOIN fp.hopeMenu hopeMenu " +
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
            "AND (:mateTime IS NULL OR mateTime.mateTime IN :mateTime) " +
            "AND (:menus IS NULL OR hopeMenu.menus IN :menus) " +
            "AND (:mealTime IS NULL OR fp.mealTime IN :mealTime) " +
            "AND (:diningManner IS NULL OR fp.diningManner = :diningManner) "
    )
    List<FoodMateProfile> findProfilesByFilters(
            @Param("schoolId") Long schoolId,
            @Param("gender") Integer gender,
            @Param("minBirthYear") String minBirthYear,
            @Param("maxBirthYear") String maxBirthYear,
            @Param("minEntryYear") String minEntryYear,
            @Param("maxEntryYear") String maxEntryYear,
            @Param("mbti") String mbti,
            @Param("colleges") Set<String> colleges,
            @Param("mateTime") Set<MateTime> mateTime,
            @Param("menus") Set<Menu> menus,
            @Param("mealTime") Set<MealTime> mealTime,
            @Param("diningManner") DiningManner diningManner
    );

}
