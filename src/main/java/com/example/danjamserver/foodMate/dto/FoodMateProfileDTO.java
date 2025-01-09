package com.example.danjamserver.foodMate.dto;

import com.example.danjamserver.foodMate.domain.FoodMateProfile;
import com.example.danjamserver.foodMate.domain.HopeMenu;
import com.example.danjamserver.foodMate.domain.MealMateTime;
import com.example.danjamserver.foodMate.enums.DiningManner;
import com.example.danjamserver.foodMate.enums.MateTime;
import com.example.danjamserver.foodMate.enums.MealTime;
import com.example.danjamserver.foodMate.enums.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodMateProfileDTO {

    private Long id;
    private Long userId;
    private Set<MateTime> mateTime;
    private Set<Menu> hopeMenu;
    private MealTime mealTime;
    private DiningManner diningManner;
    private String allergies;

    // FoodMateProfile을 FoodMateProfileDTO로 전환하는 메서드. cache에 꼭 필요한 데이터만 저장하기 위해 변환
    public static List<FoodMateProfileDTO> of(List<FoodMateProfile> foodMateProfiles){
        return foodMateProfiles.stream()
                .map(foodMateProfile -> FoodMateProfileDTO.builder()
                        .id(foodMateProfile.getId())
                        .userId(foodMateProfile.getUser().getId())
                        .mateTime(foodMateProfile.getMateTime().stream().map(MealMateTime::getMateTime).collect(Collectors.toSet()))
                        .hopeMenu(foodMateProfile.getHopeMenu().stream().map(HopeMenu::getMenus).collect(Collectors.toSet()))
                        .mealTime(foodMateProfile.getMealTime())
                        .diningManner(foodMateProfile.getDiningManner())
                        .allergies(foodMateProfile.getAllergies())
                        .build()
                ).collect(Collectors.toList());
    }

}
