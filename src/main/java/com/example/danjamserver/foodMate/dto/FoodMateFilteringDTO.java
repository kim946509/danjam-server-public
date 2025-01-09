package com.example.danjamserver.foodMate.dto;

import com.example.danjamserver.foodMate.enums.DiningManner;
import com.example.danjamserver.foodMate.enums.MateTime;
import com.example.danjamserver.foodMate.enums.MealTime;

import java.util.Map;
import java.util.Set;

import com.example.danjamserver.foodMate.enums.Menu;
import com.example.danjamserver.mate.dto.BaseFilteringDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class FoodMateFilteringDTO extends BaseFilteringDTO {
//    private String mbti;
//    private String minBirthYear;
//    private String maxBirthYear;
//    private Integer minEntryYear;
//    private Integer maxEntryYear;
//    private List<String> colleges;
    private Set<Integer> gender; // 0: 여자, 1: 남자. 성별추가
    private Set<MateTime> mateTime;
    private Set<Menu> menus;
    private Set<MealTime> mealTime;
    private DiningManner diningManner;
    private Map<String, Object> allergies;
}
