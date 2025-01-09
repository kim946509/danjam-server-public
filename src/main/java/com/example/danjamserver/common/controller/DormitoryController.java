package com.example.danjamserver.common.controller;

import com.example.danjamserver.common.service.DormitoryService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dormitory")
public class DormitoryController {
    private final DormitoryService dormitoryService;

    public DormitoryController(DormitoryService dormitoryService) {
        this.dormitoryService = dormitoryService;
    }

    @GetMapping
    public ResponseEntity<RestResponse<?>> getDormitoryInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        RestResponse<?> response = dormitoryService.getDormitoryInfo(customUserDetails);

        if (response.getCode() != 200) {
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
