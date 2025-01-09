package com.example.danjamserver.mate.controller;

import com.example.danjamserver.foodMate.dto.FoodMateProfileInputDTO;
import com.example.danjamserver.roomMate.dto.RoomMateProfileInputDTO;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.studyMate.dto.StudyMateProfileInputDTO;
import com.example.danjamserver.mate.service.MateProfileModifyService;
import com.example.danjamserver.util.response.ApiResponseMessage;
import com.example.danjamserver.walkMate.dto.WalkMateProfileInputDTO;
import com.example.danjamserver.workoutMate.dto.WorkoutMateProfileInputDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MateProfileController {

    private final MateProfileModifyService mateProfileModifyService;

    @PutMapping("/mateProfile/roomMate")
    public ResponseEntity<ApiResponseMessage> updateRoomMateProfile(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid RoomMateProfileInputDTO roomMateProfileInputDTO) {
        mateProfileModifyService.updateRoomMateProfile(roomMateProfileInputDTO, customUserDetails);
        return ResponseEntity.ok(ApiResponseMessage.of("룸 메이트 정보가 정상적으로 업데이트 되었습니다."));
    }

    @PutMapping("/mateProfile/foodMate")
    public ResponseEntity<ApiResponseMessage> updateFoodMateProfile(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid FoodMateProfileInputDTO foodMateProfile) {
        mateProfileModifyService.updateFoodMateProfile(foodMateProfile, customUserDetails);
        return ResponseEntity.ok(ApiResponseMessage.of("식사 메이트 정보가 정상적으로 업데이트 되었습니다."));
    }

    @PutMapping("/mateProfile/walkMate")
    public ResponseEntity<ApiResponseMessage> updateWalkMateProfile(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid WalkMateProfileInputDTO walkMateProfile) {
        mateProfileModifyService.updateWalkMateProfile(walkMateProfile, customUserDetails);
        return ResponseEntity.ok(ApiResponseMessage.of("산책 메이트 정보가 정상적으로 업데이트 되었습니다."));
    }

    @PutMapping("/mateProfile/workoutMate")
    public ResponseEntity<ApiResponseMessage> updateWorkoutMateProfile(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid WorkoutMateProfileInputDTO workoutMateProfile) {
        mateProfileModifyService.updateWorkoutMateProfile(workoutMateProfile, customUserDetails);
        return ResponseEntity.ok(ApiResponseMessage.of("운동 메이트 정보가 정상적으로 업데이트 되었습니다."));
    }

    @PutMapping("/mateProfile/studyMate")
    public ResponseEntity<ApiResponseMessage> updateStudytMateProfile(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid StudyMateProfileInputDTO studyMateProfile) {
        mateProfileModifyService.updateStudyMateProfile(studyMateProfile, customUserDetails);
        return ResponseEntity.ok(ApiResponseMessage.of("스터디 메이트 정보가 정상적으로 업데이트 되었습니다."));
    }
}
