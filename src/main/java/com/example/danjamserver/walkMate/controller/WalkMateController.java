package com.example.danjamserver.walkMate.controller;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseData;
import com.example.danjamserver.util.response.ApiResponseMessage;
import com.example.danjamserver.walkMate.dto.WalkMateCandidateDetailDTO;
import com.example.danjamserver.walkMate.dto.WalkMateFilteringDTO;
import com.example.danjamserver.walkMate.dto.WalkMateProfileInputDTO;
import com.example.danjamserver.walkMate.enums.WalkIntensity;
import com.example.danjamserver.walkMate.enums.WalkTime;
import com.example.danjamserver.walkMate.enums.WalkTimeZone;
import com.example.danjamserver.walkMate.service.WalkMateCandidateService;
import com.example.danjamserver.walkMate.service.WalkMateProfileService;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate/walkmate")
public class WalkMateController {

    private final WalkMateProfileService walkMateProfileService;
    private final WalkMateCandidateService walkMateCandidateService;

    // 산책 메이트 프로필 입력
    @PostMapping("/profile")
    public ResponseEntity<ApiResponseMessage> createWalkMateProfile(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid WalkMateProfileInputDTO walkMateProfileInputDTO) {
        walkMateProfileService.createWalkMateProfile(customUserDetails, walkMateProfileInputDTO);
        return ResponseEntity.ok().body(ApiResponseMessage.of("성공적으로 저장되었습니다."));
    }

    // 산책 메이트 후보자 조회
    @GetMapping("/candidate")
    public ResponseEntity<ApiResponseData<Set<WalkMateCandidateDetailDTO>>> getWalkMateCandidates(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            , @RequestParam(value = "mbti", required = false) String mbti
            , @RequestParam(value = "gender", required = false) Set<Integer> gender
            , @RequestParam(value = "minBirthYear", required = false) String minBirthYear
            , @RequestParam(value = "maxBirthYear", required = false) String maxBirthYear
            , @RequestParam(value = "minEntryYear", required = false) String minEntryYear
            , @RequestParam(value = "maxEntryYear", required = false) String maxEntryYear
            , @RequestParam(value = "colleges", required = false) Set<String> colleges
            , @RequestParam(value = "preferredWalkTimeZones", required = false) Set<WalkTimeZone> preferredWalkTimeZones
            ,
            @RequestParam(value = "preferredWalkIntensities", required = false) Set<WalkIntensity> preferredWalkIntensities
            , @RequestParam(value = "preferredWalkTimes", required = false) Set<WalkTime> preferredWalkTimes
    ) {
        WalkMateFilteringDTO walkMateFilteringDTO = WalkMateFilteringDTO.builder()
                .mbti(mbti)
                .gender(gender)
                .minBirthYear(minBirthYear)
                .maxBirthYear(maxBirthYear)
                .minEntryYear(minEntryYear)
                .maxEntryYear(maxEntryYear)
                .colleges(colleges)
                .preferredWalkTimeZones(preferredWalkTimeZones)
                .preferredWalkIntensities(preferredWalkIntensities)
                .preferredWalkTimes(preferredWalkTimes)
                .build();

        Set<WalkMateCandidateDetailDTO> walkMateCandidates = walkMateCandidateService.getWalkMateCandidates(
                customUserDetails, walkMateFilteringDTO);
        return ResponseEntity.ok().body(ApiResponseData.of(walkMateCandidates, "성공적으로 조회되었습니다."));
    }
}
