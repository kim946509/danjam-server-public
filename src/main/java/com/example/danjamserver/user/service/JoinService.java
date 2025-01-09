package com.example.danjamserver.user.service;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.repository.MyProfileRepository;
import com.example.danjamserver.common.service.MinioService;
import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.dto.JoinDTO;
import com.example.danjamserver.common.repository.SchoolRepository;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.springSecurity.role.Role;
import com.example.danjamserver.util.exception.InvalidInputException;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SchoolRepository schoolRepository;
    private final MyProfileRepository myProfileRepository;
    private final MinioService minioService;

    /**
     * 사용자 정보를 저장합니다.
     *
     * @param joinDTO 사용자 가입 정보
     * @return 인증 이미지 URL
     * @throws Exception 파일 업로드 또는 데이터베이스 오류 시 예외 발생
     */
    @Transactional
    public String registerUser(JoinDTO joinDTO) throws Exception {
        // 비밀번호 암호화
        joinDTO.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));

        // 파일 업로드 처리
        String authImgExtension = minioService.getFileExtension(joinDTO.getAuthImgFile());
        String residentImgExtension = minioService.getFileExtension(joinDTO.getResidentImgFile());

        String authImgFileName = minioService.generateFileName(joinDTO.getUsername(), "auth", authImgExtension);
        String residentImgFileName = minioService.generateFileName(joinDTO.getUsername(), "resident",
                residentImgExtension);

        String authImgUrl = minioService.uploadFile(joinDTO.getAuthImgFile(), "user-auth-img", authImgFileName);
        String residentImgUrl = minioService.uploadFile(joinDTO.getResidentImgFile(), "user-resident-img",
                residentImgFileName);

        // 학교 정보 조회
        School school = schoolRepository.findById(joinDTO.getSchoolId())
                .orElseThrow(() -> new RuntimeException("학교를 찾을 수 없습니다. ID: " + joinDTO.getSchoolId()));

        // 사용자 및 프로필 객체 생성
        User user = buildUser(joinDTO, authImgUrl, residentImgUrl, school);
        MyProfile myProfile = build(joinDTO, user);
        user.setMyProfile(myProfile);

        // 유저와 프로필 저장
        userRepository.save(user);
        myProfileRepository.save(myProfile);

        return authImgUrl;
    }

    /**
     * 사용자 정보에 맞게 User 객체를 생성합니다.
     *
     * @param joinDTO        사용자 가입 정보
     * @param authImgUrl     학생증 이미지 파일 url
     * @param residentImgUrl 주민등록증 이미지 파일 url
     * @param school         유저의 학교 정보
     * @return User 객체
     */
    private User buildUser(JoinDTO joinDTO, String authImgUrl, String residentImgUrl, School school) {
        return User.builder()
                .school(school)
                .role(Role.STRANGER)
                .username(joinDTO.getUsername())
                .nickname(joinDTO.getNickname())
                .password(joinDTO.getPassword())
                .email(joinDTO.getEmail())
                .gender(joinDTO.getGender())
                .authImgUrl(authImgUrl)
                .residentImgUrl(residentImgUrl)
                .build();
    }

    /**
     * 유저의 초기 프로필 객체를 생성합니다.
     *
     * @param joinDTO 사용자 가입 정보
     * @param user    저장되는 user 객체
     * @return 객체
     * @throws Exception 파일 업로드 또는 profile 객체 생성 오류 시 예외 발생
     */
    private MyProfile build(JoinDTO joinDTO, User user) throws Exception {
        return MyProfile.builder()
                .birth(joinDTO.getBirth())
                .entryYear(joinDTO.getEntryYear())
                .major(joinDTO.getMajor())
                .user(user)
                .build();
    }

    /**
     * 유저명이 중복되는지 확인합니다.
     *
     * @param username 유저명
     * @return 중복이면 true 아니면 false
     */
    public boolean isUsernameDuplicate(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 이메일이 중복되는지 확인합니다.
     *
     * @param email 이메일
     * @return 중복이면 true 아니면 false
     */
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 닉네임이 중복되는지 확인합니다.
     *
     * @param nickname 닉네임
     * @return 중복이면 true 아니면 false
     */
    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 유저를 탈퇴시킵니다.
     */
    @Transactional
    public void deleteAccount(CustomUserDetails customUserDetails, String password) {
        Long userId = customUserDetails.getId();

        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(InvalidTokenUser::new);
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new InvalidInputException("비밀번호가 일치하지 않습니다.");
        }
        user.setDeletedAt(LocalDateTime.now());
    }
}
