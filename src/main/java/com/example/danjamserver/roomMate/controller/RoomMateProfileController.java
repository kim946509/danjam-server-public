package com.example.danjamserver.roomMate.controller;

import com.example.danjamserver.util.response.ApiResponseMessage;
import com.example.danjamserver.roomMate.dto.RoomMateProfileInputDTO;
import com.example.danjamserver.roomMate.service.RoomMateProfileService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate")
public class RoomMateProfileController {

    private final RoomMateProfileService roomMateProfileService;
    
    @PostMapping("/roommate/profile")
    public ResponseEntity<ApiResponseMessage> createRoomMateProfile(@RequestBody @Valid RoomMateProfileInputDTO roomMateProfileInputDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        roomMateProfileService.createRoomMateProfile(roomMateProfileInputDTO, customUserDetails);
        return ResponseEntity.ok().body(ApiResponseMessage.of("성공적으로 저장되었습니다."));
    }
}
