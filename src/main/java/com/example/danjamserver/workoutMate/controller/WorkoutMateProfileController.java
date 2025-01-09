package com.example.danjamserver.workoutMate.controller;

import com.example.danjamserver.util.response.ApiResponseMessage;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.workoutMate.dto.WorkoutMateProfileInputDTO;
import com.example.danjamserver.workoutMate.service.WorkoutMateProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate")
public class WorkoutMateProfileController {

    private final WorkoutMateProfileService workoutMateProfileService;

    @PostMapping("/workout/profile")
    public ResponseEntity<ApiResponseMessage> createRoomMateProfile(@RequestBody @Valid WorkoutMateProfileInputDTO workoutMateProfileInputDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

            workoutMateProfileService.createWorkoutMateProfile(customUserDetails, workoutMateProfileInputDTO);
            return ResponseEntity.ok().body(ApiResponseMessage.of("성공적으로 저장되었습니다."));
    }
}
