package com.example.danjamserver.studyMate.controller;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.studyMate.dto.StudyMateCandidateDetailDTO;
import com.example.danjamserver.studyMate.dto.StudyMateFilteringDTO;
import com.example.danjamserver.studyMate.enums.AverageGrade;
import com.example.danjamserver.studyMate.enums.StudyTime;
import com.example.danjamserver.studyMate.enums.StudyType;
import com.example.danjamserver.studyMate.service.StudyMateCandidateService;
import com.example.danjamserver.util.response.ApiResponseData;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate/studymate")
public class StudyMateCandidateController {

    private final StudyMateCandidateService studyMateCandidateService;

    @GetMapping("/candidate")
    public ResponseEntity<ApiResponseData<Set<StudyMateCandidateDetailDTO>>> getStudyMateCandidates(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            , @RequestParam(value = "mbti", required = false) String mbti
            , @RequestParam(value = "gender", required = false) Set<Integer> gender
            , @RequestParam(value = "minBirthYear", required = false) String minBirthYear
            , @RequestParam(value = "maxBirthYear", required = false) String maxBirthYear
            , @RequestParam(value = "minEntryYear", required = false) String minEntryYear
            , @RequestParam(value = "maxEntryYear", required = false) String maxEntryYear
            , @RequestParam(value = "colleges", required = false) Set<String> colleges
            , @RequestParam(value = "preferredStudyTypes", required = false) Set<StudyType> preferredStudyTypes
            , @RequestParam(value = "preferredStudyTimes", required = false) Set<StudyTime> preferredStudyTimes
            , @RequestParam(value = "preferredAverageGrades", required = false) Set<AverageGrade> preferredAverageGrades
    ) {
        StudyMateFilteringDTO studyMateFilteringDTO = StudyMateFilteringDTO.builder()
                .mbti(mbti)
                .gender(gender)
                .minBirthYear(minBirthYear)
                .maxBirthYear(maxBirthYear)
                .minEntryYear(minEntryYear)
                .maxEntryYear(maxEntryYear)
                .colleges(colleges)
                .preferredStudyTypes(preferredStudyTypes)
                .preferredStudyTimes(preferredStudyTimes)
                .preferredAverageGrades(preferredAverageGrades)
                .build();
        Set<StudyMateCandidateDetailDTO> studyMateCandidates = studyMateCandidateService.getStudyMateCandidates(
                customUserDetails, studyMateFilteringDTO);
        return ResponseEntity.ok().body(ApiResponseData.of(studyMateCandidates, "성공적으로 조회되었습니다."));
    }
}
