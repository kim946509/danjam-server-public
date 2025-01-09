package com.example.danjamserver.workoutMate.controller;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseData;
import com.example.danjamserver.workoutMate.dto.WorkoutMateCandiateDetailDTO;
import com.example.danjamserver.workoutMate.dto.WorkoutMateFilteringDTO;
import com.example.danjamserver.workoutMate.enums.WorkoutIntensity;
import com.example.danjamserver.workoutMate.enums.WorkoutTime;
import com.example.danjamserver.workoutMate.enums.WorkoutTimeZone;
import com.example.danjamserver.workoutMate.enums.WorkoutType;
import com.example.danjamserver.workoutMate.service.WorkoutMateCandidateService;
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
public class WorkoutMateCandidateController {

    private final WorkoutMateCandidateService roomMateCandidateService;

    @GetMapping("/workout/candidate")
    public ResponseEntity<ApiResponseData<Set<WorkoutMateCandiateDetailDTO>>> getWorkoutMateCandidate(@AuthenticationPrincipal CustomUserDetails customUserDetails
            , @RequestParam(value = "mbti", required = false) String mbti
            , @RequestParam(value = "gender", required = false) Set<Integer> gender
            , @RequestParam(value = "minBirthYear", required = false) String minBirthYear
            , @RequestParam(value = "maxBirthYear", required = false) String maxBirthYear
            , @RequestParam(value = "minEntryYear", required = false) String minEntryYear
            , @RequestParam(value = "maxEntryYear", required = false) String maxEntryYear
            , @RequestParam(value = "colleges", required = false) Set<String> colleges
            , @RequestParam(value = "preferredWorkoutTimeZones", required = false) Set<WorkoutTimeZone> preferredWorkoutTimeZones
            , @RequestParam(value = "preferredWorkoutIntensity", required = false) Set<WorkoutIntensity> preferredWorkoutIntensity
            , @RequestParam(value = "preferredWorkoutTimes", required = false) Set<WorkoutTime> preferredWorkoutTimes
            , @RequestParam(value = "preferredWorkoutTypes", required = false) Set<WorkoutType> preferredWorkoutTypes
    ) {
        WorkoutMateFilteringDTO workoutMateFilteringDTO = WorkoutMateFilteringDTO.builder()
                .mbti(mbti)
                .gender(gender)
                .minBirthYear(minBirthYear)
                .maxBirthYear(maxBirthYear)
                .minEntryYear(minEntryYear)
                .maxEntryYear(maxEntryYear)
                .colleges(colleges)
                .preferredWorkoutTimeZones(preferredWorkoutTimeZones)
                .preferredWorkoutIntensities(preferredWorkoutIntensity)
                .preferredWorkoutTypes(preferredWorkoutTypes)
                .preferredWorkoutTimes(preferredWorkoutTimes)
                .build();
        Set<WorkoutMateCandiateDetailDTO> workoutMateCandiateDetailDTOList = roomMateCandidateService.getWorkoutMateCandidateList(customUserDetails, workoutMateFilteringDTO);
        return ResponseEntity.ok().body(ApiResponseData.of(workoutMateCandiateDetailDTOList, "성공적으로 조회되었습니다."));
    }


}
