package com.example.danjamserver.admin.controller;

import com.example.danjamserver.admin.service.SchoolManagementService;
import com.example.danjamserver.admin.dto.SchoolRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/school")
@RequiredArgsConstructor
public class SchoolManagementController {

    private final SchoolManagementService schoolManagementService;


    /**
     * 새로운 학교를 등록합니다.
     *
     * @param schoolRequest 학교 정보 요청 객체
     */
    @PostMapping
    public ResponseEntity<String> registerSchool(@RequestBody SchoolRequest schoolRequest) {
        try {
            schoolManagementService.saveSchool(schoolRequest);
            return ResponseEntity.ok("학교 등록 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("학교 등록 실패: " + e.getMessage());
        }
    }

    /**
     * 주어진 ID를 가진 학교를 삭제합니다.
     *
     * @param schoolId 삭제할 학교의 ID
     * @return 삭제 결과 메시지를 포함한 ResponseEntity
     */
    @DeleteMapping("/{schoolId}")
    public ResponseEntity<String> deleteSchool(@PathVariable String schoolId) {
        try {
            schoolManagementService.deleteById(schoolId);
            return ResponseEntity.ok("학교 삭제 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("잘못된 요청: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 내부 오류");
        }
    }
}