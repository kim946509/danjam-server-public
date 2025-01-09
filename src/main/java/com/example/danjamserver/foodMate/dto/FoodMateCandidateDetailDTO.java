package com.example.danjamserver.foodMate.dto;

import com.example.danjamserver.foodMate.enums.DiningManner;
import com.example.danjamserver.foodMate.enums.MateTime;
import com.example.danjamserver.foodMate.enums.MealTime;
import com.example.danjamserver.foodMate.enums.Menu;

import java.util.Set;

import com.example.danjamserver.mate.dto.BaseCandidateDetatilDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
public class FoodMateCandidateDetailDTO extends BaseCandidateDetatilDTO {
    //User정보
//    private String nickname;

    //MyProfile 정보
//    private String mbti;
//    private String major;
//    private String greeting;
//    private Integer entryYear;
//    private String birth;
//    private String profileImgUrl;
    private Integer gender;

    //FoodMateProfile 정보
    private Set<MateTime> mateTime;
    private Set<Menu> menu;
    private MealTime mealTime;
    private DiningManner diningManner;
    private String allergies;
}
