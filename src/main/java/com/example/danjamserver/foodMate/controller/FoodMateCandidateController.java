package com.example.danjamserver.foodMate.controller;

import com.example.danjamserver.foodMate.dto.FoodMateCandidateDetailDTO;
import com.example.danjamserver.foodMate.dto.FoodMateFilteringDTO;
import com.example.danjamserver.foodMate.enums.DiningManner;
import com.example.danjamserver.foodMate.enums.MateTime;
import com.example.danjamserver.foodMate.enums.MealTime;
import com.example.danjamserver.foodMate.enums.Menu;
import com.example.danjamserver.foodMate.service.FoodMateCandidateService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate")
public class FoodMateCandidateController {

    private final FoodMateCandidateService foodMateCandidateService;

    @GetMapping("/foodmate/candidate")
    public ResponseEntity<ApiResponseData<Set<FoodMateCandidateDetailDTO>>> getRoomMateCandidate(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            , @RequestParam(value = "mbti", required = false) String mbti
            , @RequestParam(value = "gender", required = false) Set<Integer> gender // 0: 여자, 1: 남자
            , @RequestParam(value = "minBirthYear", required = false) String minBirthYear
            , @RequestParam(value = "maxBirthYear", required = false) String maxBirthYear
            , @RequestParam(value = "minEntryYear", required = false) String minEntryYear
            , @RequestParam(value = "maxEntryYear", required = false) String maxEntryYear
            , @RequestParam(value = "colleges", required = false) Set<String> colleges
            , @RequestParam(value = "mateTime", required = false) Set<MateTime> mateTime
            , @RequestParam(value = "menus", required = false) Set<Menu> menus
            , @RequestParam(value = "mealTime", required = false) Set<MealTime> mealTime
            , @RequestParam(value = "diningManner", required = false) DiningManner diningManner
    ) {
        FoodMateFilteringDTO foodMateFilteringDto = FoodMateFilteringDTO.builder()
                .mbti(mbti)
                .gender(gender)
                .minBirthYear(minBirthYear)
                .maxBirthYear(maxBirthYear)
                .minEntryYear(minEntryYear)
                .maxEntryYear(maxEntryYear)
                .colleges(colleges)
                .mateTime(mateTime)
                .menus(menus)
                .mealTime(mealTime)
                .diningManner(diningManner)
                .build();

        Set<FoodMateCandidateDetailDTO> foodMateCandidateList = foodMateCandidateService.getFoodMateCandidateList(customUserDetails, foodMateFilteringDto);

        return ResponseEntity.ok(ApiResponseData.of(foodMateCandidateList, "식사메이트 후보 리스트 조회 성공"));
    }
}