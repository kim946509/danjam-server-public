package com.example.danjamserver.mate.controller;


import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.mate.dto.MateLikeDTO;
import com.example.danjamserver.mate.dto.MateLikeResponseDTO;
import com.example.danjamserver.mate.service.MateLikeService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseMessage;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mateLikes")
@RequiredArgsConstructor
public class MateLikeController {
    private final MateLikeService mateLikeService;

    @GetMapping("/user")
    public ResponseEntity<Map<MateType, List<MateLikeResponseDTO>>> getMateLikesByUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Map<MateType, List<MateLikeResponseDTO>> mateLike = mateLikeService.readMateLikesByUser(customUserDetails);
        return ResponseEntity.ok(mateLike);
    }

    @PostMapping
    public ResponseEntity<ApiResponseMessage> saveMateLike(@RequestBody @Valid MateLikeDTO mateLikeDTO,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        mateLikeService.saveMateLike(mateLikeDTO, customUserDetails);
        return ResponseEntity.ok(ApiResponseMessage.of("찜이 성공적으로 저장되었습니다."));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponseMessage> deleteMateLike(@RequestBody @Valid MateLikeDTO mateLikeDTO,
                                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        mateLikeService.deleteMateLike(mateLikeDTO, customUserDetails);
        return ResponseEntity.ok(ApiResponseMessage.of("찜이 성공적으로 삭제되었습니다."));
    }

}
