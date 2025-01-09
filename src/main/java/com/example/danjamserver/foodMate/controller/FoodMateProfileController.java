package com.example.danjamserver.foodMate.controller;

import com.example.danjamserver.foodMate.dto.FoodMateProfileInputDTO;
import com.example.danjamserver.foodMate.service.FoodMateService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate")
public class FoodMateProfileController {

    private final FoodMateService foodMateService;

    @PostMapping("/food/profile")
    public ResponseEntity<ApiResponseMessage> createFoodMateProfile(
            @RequestBody @Valid FoodMateProfileInputDTO foodMateProfileInputDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
            foodMateService.createFoodMateProfile(foodMateProfileInputDto, customUserDetails);
            return ResponseEntity.ok().body(ApiResponseMessage.of("성공적으로 저장되었습니다."));
    }
}
