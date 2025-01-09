package com.example.danjamserver.mate.controller;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.mate.dto.FilterOutputDTO;
import com.example.danjamserver.mate.service.MateFilterService;
import com.example.danjamserver.util.response.ApiResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate")
public class FilterController {

    private final MateFilterService mateFilterService;

    // mate에서 필터링 하기위한 유저 정보를 가져옵니다.
    @GetMapping("/filter")
    // 사용자의 정보를 통해 학교, 성별, 단과대, 기숙사 정보를 조회합니다.
    public ResponseEntity<ApiResponseData<FilterOutputDTO>> getCollegesByUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        FilterOutputDTO collegesByUser = mateFilterService.getCollegesByUser(customUserDetails);
        return ResponseEntity.ok(ApiResponseData.of(collegesByUser, "학교, 단과대 정보 조회 성공"));
    }
}
