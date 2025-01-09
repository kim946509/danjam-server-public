package com.example.danjamserver.mate.controller;

import com.example.danjamserver.mate.service.MateProfileService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate")
public class MateController {

    private final MateProfileService mateProfileService;

    // 메이트 찾기가 가능한지 확인. 처음 5가지 메이트 중에서 해당 메이트 찾기를 눌렀을시 호출
    @GetMapping("{mateType}/condition")
    public ResponseEntity<ApiResponseMessage> checkUserCanFindMate(
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("mateType") String mateType) {
        mateProfileService.checkUserCanFindMate(customUserDetails, mateType);
        return ResponseEntity.ok().body(ApiResponseMessage.of("메이트 찾기가 가능합니다."));
    }
}
