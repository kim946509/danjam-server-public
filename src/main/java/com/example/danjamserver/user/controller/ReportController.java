package com.example.danjamserver.user.controller;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.dto.ReportDTO;
import com.example.danjamserver.user.dto.ReportResponseDTO;
import com.example.danjamserver.user.service.ReportService;
import com.example.danjamserver.util.response.ApiResponseMessage;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/user/report")
    public ResponseEntity<ApiResponseMessage> createReport(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                           @RequestBody @Valid ReportDTO reportDTO) {
        reportService.createReport(reportDTO, customUserDetails);
        return ResponseEntity.ok(ApiResponseMessage.of("신고가 성공적으로 저장되었습니다."));
    }

    // 특정 유저의 신고 내역 조회
    @GetMapping("/admin/report/{username}")
    public ResponseEntity<Map<String, List<ReportResponseDTO>>> readUserReport(
            @AuthenticationPrincipal @PathVariable String username) {
        return ResponseEntity.ok(reportService.readReport(username));
    }

    // 전체 신고 내역 조회
    @GetMapping("/admin/report")
    public ResponseEntity<Map<String, List<ReportResponseDTO>>> readUsersReport(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(reportService.readReport());
    }

}
