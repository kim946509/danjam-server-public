package com.example.danjamserver.user.controller;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.dto.BlockDTO;
import com.example.danjamserver.user.dto.BlockResponseDTO;
import com.example.danjamserver.user.service.BlockService;
import com.example.danjamserver.util.response.ApiResponseMessage;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @PostMapping("/user/block")
    public ResponseEntity<ApiResponseMessage> toggleBlock(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                          @RequestBody @Valid BlockDTO blockDTO) {
        return ResponseEntity.ok(ApiResponseMessage.of(blockService.toggleBlockStatus(customUserDetails, blockDTO)));
    }

    @GetMapping("/admin/block")
    public ResponseEntity<List<BlockResponseDTO>> getBlockedUsersByBlocker(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(blockService.getBlockedUsersByBlocker());
    }
}
