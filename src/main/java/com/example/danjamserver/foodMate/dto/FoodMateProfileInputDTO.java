package com.example.danjamserver.foodMate.dto;

import com.example.danjamserver.foodMate.domain.FoodMateProfile;
import com.example.danjamserver.foodMate.domain.HopeMenu;
import com.example.danjamserver.foodMate.domain.MealMateTime;
import com.example.danjamserver.foodMate.enums.DiningManner;
import com.example.danjamserver.foodMate.enums.MateTime;
import com.example.danjamserver.foodMate.enums.MealTime;
import com.example.danjamserver.foodMate.enums.Menu;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class FoodMateProfileInputDTO {

    @NotNull(message = "식사메이트 시간을 선택해주세요.")
    private Set<MateTime> mateTime; // 식사메이트 필요시간

    @NotNull(message = "선호하는 음식을 선택해주세요.")
    private Set<Menu> menu; // 선호 음식

    @NotNull(message = "식사하는 데 걸리는 시간을 선택해주세요.")
    @Enumerated(EnumType.STRING)
    private MealTime mealTime; // 식사 시간

    @NotNull(message = "선호하는 식사매너를 선택해주세요.")
    private DiningManner diningManner; // 식사 매너
    private Map<String, Object> allergies;

    // 정적 팩토리 메서드
    public static FoodMateProfileInputDTO from(FoodMateProfile foodMateProfile) {
        FoodMateProfileInputDTO profile = new FoodMateProfileInputDTO();

        profile.setMateTime(foodMateProfile.getMateTime().stream()
                .map(MealMateTime::getMateTime)
                .collect(Collectors.toSet()));

        profile.setMenu(foodMateProfile.getHopeMenu().stream()
                .map(HopeMenu::getMenus)
                .collect(Collectors.toSet()));

        profile.setMealTime(foodMateProfile.getMealTime());
        profile.setDiningManner(foodMateProfile.getDiningManner());
        profile.setAllergies(parseAllergies(foodMateProfile.getAllergies()));

        return profile;
    }

    // JSON 문자열을 Map<String, Object>으로 변환하는 헬퍼 메서드
    private static Map<String, Object> parseAllergies(String allergiesJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(allergiesJson, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse allergies JSON string", e);
        }
    }
}
