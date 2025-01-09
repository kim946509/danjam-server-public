package com.example.danjamserver.user.controller;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.dto.JoinDTO;
import com.example.danjamserver.user.dto.PasswordDTO;
import com.example.danjamserver.user.service.JoinService;
import com.example.danjamserver.util.response.ApiResponseMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    /**
     * 사용자 가입을 처리합니다.
     *
     * @param joinDTO       사용자 가입 정보
     * @param bindingResult 유효성 검사 결과
     * @return 인증 이미지 URL을 포함한 응답
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> registerUser(@ModelAttribute @Valid JoinDTO joinDTO,
                                                            BindingResult bindingResult) {

        // 유효성 검사 실패 시 에러 메시지 반환
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            bindingResult.getAllErrors().forEach(objectError -> {
                sb.append(objectError.getDefaultMessage()).append("\n");
            });
            return ResponseEntity.badRequest().body(Map.of("error", sb.toString()));
        }

        try {
            String returnAuthUrl = joinService.registerUser(joinDTO);
            Map<String, String> response = new HashMap<>();
            response.put("url", returnAuthUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 유저명 중복 여부를 확인합니다.
     *
     * @param username 확인할 유저명
     * @return 중복되지 않으면 true, 중복되면 false
     */
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameAvailability(@RequestParam("username") String username) {
        return ResponseEntity.ok(!joinService.isUsernameDuplicate(username));
    }

    /**
     * 이메일 중복 여부를 확인합니다.
     *
     * @param email 확인할 이메일
     * @return 중복되지 않으면 true, 중복되면 false
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam("email") String email) {
        return ResponseEntity.ok(!joinService.isEmailDuplicate(email));
    }

    /**
     * 닉네임 중복 여부를 확인합니다.
     *
     * @param nickname 확인할 닉네임
     * @return 중복되지 않으면 true, 중복되면 false
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNicknameAvailability(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(!joinService.isNicknameDuplicate(nickname));
    }

    /**
     * 사용자 탈퇴 처리를 합니다.
     *
     * @param customUserDetails
     * @return 성공 메시지
     */
    @DeleteMapping("/account")
    public ResponseEntity<ApiResponseMessage> deleteAccount(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid PasswordDTO passwordDTO) {

        joinService.deleteAccount(customUserDetails, passwordDTO.getPassword());
        return ResponseEntity.ok().body(ApiResponseMessage.of("성공적으로 회원이 탈퇴되었습니다."));
    }
}