package com.example.danjamserver.common.controller;


import com.example.danjamserver.common.dto.UserInfoResponseDTO;
import com.example.danjamserver.common.service.UserInfoService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponseDTO> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                           @RequestParam(value = "username", required = false) String username) {
        return ResponseEntity.ok(userInfoService.getUserInfo(username).getData());
    }

    @GetMapping("/nickname")
    public ResponseEntity<String> getUserNickname(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userInfoService.getUserNickname(customUserDetails).getData());
    }
}
