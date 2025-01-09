package com.example.danjamserver.user.controller;

import com.example.danjamserver.user.dto.*;
import com.example.danjamserver.user.service.MyProfileService;
import com.example.danjamserver.util.response.ApiResponseMessage;
import com.example.danjamserver.util.response.RestResponse;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/myProfile")
public class MyProfileController {

    private final MyProfileService myProfileService;

    /**
     * 프로필 이미지를 업로드합니다.
     *
     * @param file              프로필 이미지 파일
     * @param customUserDetails 현재 인증된 사용자 정보
     * @return 프로필 이미지 업로드 성공 메시지 또는 실패 메시지
     */
    @PostMapping("/profileImg")
    public ResponseEntity<String> uploadProfileImage(@RequestBody MultipartFile file,
                                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            RestResponse<String> response = myProfileService.uploadProfileImage(file, customUserDetails);
            return ResponseEntity.ok("프로필 이미지 업로드 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("프로필 이미지 업로드 실패: " + e.getMessage());
        }
    }

    /**
     * 회원가입 때 프로필은 이미 생성되어있음. 프로필에 대한 추가 정보를 넣어주기 위한 api(mbti, greeting)
     *
     * @param myProfileDTO
     * @param customUserDetails
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponseMessage> createProfileProc(@RequestBody @Valid MyProfileDTO myProfileDTO,
                                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        myProfileService.setProfile(customUserDetails, myProfileDTO);
        return ResponseEntity.ok(ApiResponseMessage.of("프로필이 입력되었습니다."));
    }

    /**
     * 마이 페이지 정보 보기
     *
     * @param customUserDetails
     * @return MyProfileInfoDTO
     */
    @GetMapping
    public ResponseEntity<MyProfileInfoDTO> readProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(myProfileService.readMyProfileInfo(customUserDetails));
    }

    /**
     * 마이 페이지 메이트 정보 보기
     *
     * @param customUserDetails
     * @return MyProfileInfoDTO
     */
    @GetMapping("/mate")
    public ResponseEntity<MyProfileMateInfoDTO> readProfileMate(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(myProfileService.readMyProfileMateInfo(customUserDetails));
    }

    /**
     * 프로필 수정 mbti, 소개글
     *
     * @param myMbtiDTO
     * @param customUserDetails
     * @return
     */
    @PutMapping("/mbti")
    public ResponseEntity<ApiResponseMessage> updateProfileMyMbti(@RequestBody @Valid MyMbtiDTO myMbtiDTO,
                                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok().body(myProfileService.updateProfile(customUserDetails, myMbtiDTO));
    }

    /**
     * 프로필 수정 아이디, 비밀번호, 프로필 사진
     *
     * @param loginDTO
     * @param customUserDetails
     * @return
     */
    @PatchMapping("/myInfo")
    public ResponseEntity<ApiResponseMessage> updateProfileMyInfo(LoginInfoDTO loginDTO,
                                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                  @RequestPart(required = false) MultipartFile file) throws Exception {
        return ResponseEntity.ok().body(myProfileService.updateUserLoginInfo(customUserDetails, loginDTO, file));
    }

    /**
     * 프로필 수정 학교, 학과, 학교인증사진
     *
     * @param schoolInfoDto
     * @param customUserDetails
     * @return
     */
    @PutMapping("/school")
    public ResponseEntity<ApiResponseMessage> updateProfileSchool(@Valid SchoolInfoDTO schoolInfoDto,
                                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                  MultipartFile file) throws Exception {
        return ResponseEntity.ok().body(myProfileService.updateUserSchoolInfo(customUserDetails, schoolInfoDto, file));
    }

    @GetMapping("/profileImg")
    public ResponseEntity<ApiResponseMessage> readProfileImgUrl(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(ApiResponseMessage.of(myProfileService.readProfileImgUrl(customUserDetails)));
    }
}