package com.example.danjamserver.admin.controller;

import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.admin.dto.UserAuthenticationRequest;
import com.example.danjamserver.admin.service.UserManagementService;
import com.example.danjamserver.util.response.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserManagementService userManagementService;

    /**
     * 특정 기준에 따라 사용자를 검색합니다.
     *
     * @param criteria 검색 기준
     * @return 검색된 사용자 목록을 포함한 응답
     */
    @GetMapping
    public ResponseEntity<RestResponse<List<User>>> getUsersByCriteria(@RequestParam String criteria) {
        RestResponse<List<User>> response = userManagementService.retrieveUsersBasedOnCriteria(criteria);
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    /**
     * 낯선 사용자를 인증 처리합니다.
     *
     * @param authRequest 사용자 인증 요청 정보
     * @return 인증 처리 결과를 포함한 응답
     */
    @PostMapping("/strangers/authenticate")
    public ResponseEntity<RestResponse<String>> authenticateUser(HttpServletRequest request, @RequestBody UserAuthenticationRequest authRequest)
            throws IOException {
        RestResponse<String> response;
        if (Boolean.TRUE.equals(authRequest.getIsApproved())) {
            response = userManagementService.promoteUserToAuthorizedStatus(request, authRequest.getUsername());
        } else {
            response = userManagementService.denyUserAuthorization(request, authRequest);
        }
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
