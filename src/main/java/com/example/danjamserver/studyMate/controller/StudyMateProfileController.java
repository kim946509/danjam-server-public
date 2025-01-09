package com.example.danjamserver.studyMate.controller;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.studyMate.dto.StudyMateProfileInputDTO;
import com.example.danjamserver.studyMate.service.StudyMateProfileService;
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
@RequestMapping("/api/mate/studymate")
public class StudyMateProfileController {

    private final StudyMateProfileService studyMateProfileService;

    @PostMapping("/profile")
    public ResponseEntity<ApiResponseMessage> createStudyMateProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody @Valid StudyMateProfileInputDTO studyMateProfileInputDTO) {
        studyMateProfileService.createStudyMateProfile(customUserDetails, studyMateProfileInputDTO);
        return ResponseEntity.ok().body(ApiResponseMessage.of("성공적으로 저장되었습니다."));
    }


}
