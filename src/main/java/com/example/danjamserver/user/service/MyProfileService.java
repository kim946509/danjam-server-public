package com.example.danjamserver.user.service;

import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.common.repository.SchoolRepository;
import com.example.danjamserver.common.service.MinioService;
import com.example.danjamserver.foodMate.domain.FoodMateProfile;
import com.example.danjamserver.foodMate.dto.FoodMateProfileInputDTO;
import com.example.danjamserver.foodMate.repository.FoodMateProfileRepository;
import com.example.danjamserver.roomMate.domain.RoomMateProfile;
import com.example.danjamserver.roomMate.dto.RoomMateProfileInputDTO;
import com.example.danjamserver.roomMate.repository.RoomMateProfileRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.springSecurity.role.Role;
import com.example.danjamserver.studyMate.domain.StudyMateProfile;
import com.example.danjamserver.studyMate.dto.StudyMateProfileInputDTO;
import com.example.danjamserver.studyMate.repository.StudyMateProfileRepository;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.dto.LoginInfoDTO;
import com.example.danjamserver.user.dto.MyMbtiDTO;
import com.example.danjamserver.user.dto.MyProfileDTO;
import com.example.danjamserver.user.dto.MyProfileInfoDTO;
import com.example.danjamserver.user.dto.MyProfileMateInfoDTO;
import com.example.danjamserver.user.dto.SchoolInfoDTO;
import com.example.danjamserver.user.repository.MyProfileRepository;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.CustomValidationException;
import com.example.danjamserver.util.exception.FileNotFoundException;
import com.example.danjamserver.util.exception.InvalidInputException;
import com.example.danjamserver.util.exception.InvalidRequestException;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import com.example.danjamserver.util.exception.ResultCode;
import com.example.danjamserver.util.exception.UserNotFoundException;
import com.example.danjamserver.util.response.ApiResponseMessage;
import com.example.danjamserver.util.response.RestResponse;
import com.example.danjamserver.walkMate.domain.WalkMateProfile;
import com.example.danjamserver.walkMate.dto.WalkMateProfileInputDTO;
import com.example.danjamserver.walkMate.repository.WalkMateProfileRepository;
import com.example.danjamserver.workoutMate.domain.WorkoutMateProfile;
import com.example.danjamserver.workoutMate.dto.WorkoutMateProfileInputDTO;
import com.example.danjamserver.workoutMate.repository.WorkoutMateProfileRepository;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MyProfileService {

    private final MyProfileRepository myProfileRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final MinioService minioService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoomMateProfileRepository roomMateProfileRepository;
    private final FoodMateProfileRepository foodMateProfileRepository;
    private final WalkMateProfileRepository walkMateProfileRepository;
    private final WorkoutMateProfileRepository workoutMateProfileRepository;
    private final StudyMateProfileRepository studyMateProfileRepository;

    /**
     * 유저 프로필 소개글 입력 로그인한 유저의 mbti, 소개글을 입력합니다.
     *
     * @param customUserDetails
     * @param myProfileDTO
     */
    @Transactional
    public void setProfile(CustomUserDetails customUserDetails,
                           MyProfileDTO myProfileDTO) {
        String username = customUserDetails.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(InvalidTokenUser::new);

        MyProfile myProfile = user.getMyProfile();

        //이미 MyProfile에 정보가 입력됐는지 확인
        if (myProfile.getMbti() != null) {
            throw new InvalidRequestException(ResultCode.ALREADY_EXIST_PROFILE);
        }
        myProfile.setMbti(myProfileDTO.getMbti());
        myProfile.setGreeting(myProfileDTO.getGreeting());

        myProfileRepository.save(myProfile);
    }

    /**
     * 유저 프로필 소개글 수정 로그인한 유저의 mbti, 소개글을 수정합니다.
     *
     * @param customUserDetails
     * @param myMbtiDTO
     * @return
     */
    @Transactional
    public ApiResponseMessage updateProfile(CustomUserDetails customUserDetails, MyMbtiDTO myMbtiDTO) {
        String username = customUserDetails.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(InvalidTokenUser::new);

        MyProfile myProfile = user.getMyProfile();

        myProfile.setMbti(myMbtiDTO.getMbti());
        myProfile.setGreeting(myMbtiDTO.getGreeting());

        myProfileRepository.save(myProfile);
        return ApiResponseMessage.of("회원 정보가 정상적으로 업데이트 되었습니다.");
    }

    /**
     * 사용자의 프로필 이미지를 업로드합니다.
     *
     * @param file              업로드할 프로필 이미지 파일
     * @param customUserDetails 현재 인증된 사용자 정보
     * @return 업로드된 프로필 이미지의 URL을 포함한 성공 응답
     * @throws Exception 파일 업로드 중 발생한 예외
     */
    @Transactional
    public RestResponse<String> uploadProfileImage(MultipartFile file, CustomUserDetails customUserDetails)
            throws Exception {
        // 인증된 사용자 정보와 프로필 가져오기
        User user = userRepository.findByUsername(customUserDetails.getUsername())
                .orElseThrow(InvalidTokenUser::new);
        MyProfile myProfile = user.getMyProfile();

        // 파일 업로드 및 URL 저장
        String profileImgUrl = minioService.uploadFile(
                file,
                "user-profile-img",
                minioService.generateFileName(user.getUsername(), "auth", minioService.getFileExtension(file))
        );

        // DB에 프로필 이미지 URL 업데이트
        myProfileRepository.updateProfileImgUrl(myProfile.getId(), profileImgUrl);

        return RestResponse.success(profileImgUrl);
    }

    /**
     * 사용자의 username, password, profile-img 를 변경합니다.
     *
     * @param customUserDetails
     * @param loginInfoDTO
     * @param file
     * @return
     * @throws Exception
     */
    @Transactional
    public ApiResponseMessage updateUserLoginInfo(CustomUserDetails customUserDetails, LoginInfoDTO loginInfoDTO,
                                                  MultipartFile file) throws Exception {
        try {
            // 유효성 검사
            if (isInvalidInput(loginInfoDTO, file)) {
                throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "입력 데이터가 존재하지 않습니다.");
            }

            String username = customUserDetails.getUsername();
            User user = userRepository.findByUsername(username).orElseThrow(InvalidTokenUser::new);

            MyProfile myProfile = user.getMyProfile();

            if (file != null && !file.isEmpty()) {
                String authImgExtension = minioService.getFileExtension(file);
                String authImgFileName = minioService.generateFileName(username, "auth", authImgExtension);

                String profileImgUrl = minioService.uploadFile(file, "user-profile-img", authImgFileName);
                myProfile.setProfileImgUrl(profileImgUrl);
                myProfileRepository.save(myProfile);
            }

            if (loginInfoDTO != null) {
                Optional.ofNullable(loginInfoDTO.getUsername())
                        .ifPresent(userName -> {
                            if (userRepository.findByUsername(userName).isPresent()) {
                                throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "이미 사용 중인 사용자 이름입니다.");
                            }
                            user.setUsername(userName);
                        });

                Optional.ofNullable(loginInfoDTO.getPassword())
                        .ifPresent(password -> user.setPassword(bCryptPasswordEncoder.encode(password)));
            }

            userRepository.save(user);

            return ApiResponseMessage.of("회원 정보가 정상적으로 업데이트 되었습니다.");
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * 사용자의 학과, 학번, 입학년도, 학교 인증 사진을 변경합니다.
     *
     * @param customUserDetails
     * @param schoolInfoDto
     * @param file
     * @return
     * @throws Exception
     */
    @Transactional
    public ApiResponseMessage updateUserSchoolInfo(CustomUserDetails customUserDetails, SchoolInfoDTO schoolInfoDto,
                                                   MultipartFile file) throws Exception {
        try {
            String username = customUserDetails.getUsername();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("해당 이름의 사용자가 존재하지 않습니다."));
            MyProfile myProfile = user.getMyProfile();

            School school = schoolRepository.findById(schoolInfoDto.getSchoolId())
                    .orElseThrow(() -> new UserNotFoundException("해당 사용자의 학교 정보가 존재하지 않습니다."));

            // 파일이 없을 경우 InvalidInputException을 던짐
            if (file == null || file.isEmpty()) {
                throw new InvalidInputException("파일이 존재하지 않습니다.");
            }

            String authImgExtension = minioService.getFileExtension(file);
            String authImgFileName = minioService.generateFileName(username, "auth", authImgExtension);

            String authImgUrl = minioService.uploadFile(file, "user-auth-img", authImgFileName);

            myProfile.setEntryYear(schoolInfoDto.getEntryYear());
            myProfile.setMajor(schoolInfoDto.getMajor());
            user.setSchool(school);
            user.setMyProfile(myProfile);
            user.setAuthImgUrl(authImgUrl);
            user.setRole(Role.STRANGER);

            userRepository.save(user);
            myProfileRepository.save(myProfile);

            return ApiResponseMessage.of("회원 정보가 정상적으로 업데이트 되었습니다.");
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * 유저 정보 조회(전공, 소개글, mbti, 성별) 로그인한 유저의 전공, 소개글, mbti, 성별을 조회합니다.
     *
     * @param customUserDetails
     * @return
     */
    public MyProfileInfoDTO readMyProfileInfo(CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();

        //40201 error
        User user = userRepository.findByUsername(username).orElseThrow(InvalidTokenUser::new);

        MyProfile myProfile = user.getMyProfile();

        // 반환 객체 생성
        MyProfileInfoDTO myProfileInfoDTO = new MyProfileInfoDTO();

        myProfileInfoDTO.setProfileImgUrl(myProfile.getProfileImgUrl());
        myProfileInfoDTO.setNickname(user.getNickname());
        myProfileInfoDTO.setBirth(myProfile.getBirth());

        if (myProfile.getMajor() == null) {
            myProfileInfoDTO.setMajorState("전공이 입력되지 않았습니다.");
        } else {
            myProfileInfoDTO.setMajor(myProfile.getMajor());
        }
        if (myProfile.getGreeting() == null) {
            myProfileInfoDTO.setGreetingState("소개글이 입력되지 않았습니다.");
        } else {
            myProfileInfoDTO.setGreeting(myProfile.getGreeting());
        }

        if (myProfile.getMbti() == null) {
            myProfileInfoDTO.setMbtiState("mbti가 입력되지 않았습니다.");
        } else {
            myProfileInfoDTO.setMbti(myProfile.getMbti());
        }

        if (user.getGender() == null) {
            myProfileInfoDTO.setGenderState("성별이 입력되지 않았습니다.");
        } else {
            myProfileInfoDTO.setGender(user.getGender());
        }

        return myProfileInfoDTO;
    }


    /**
     * 유저 메이트 정보 조회 로그인한 유저의 메이트 입력 정보를 조회합니다.
     *
     * @param customUserDetails
     * @return
     */
    public MyProfileMateInfoDTO readMyProfileMateInfo(CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();

        //40201 error
        User user = userRepository.findByUsername(username).orElseThrow(InvalidTokenUser::new);

        // 반환 객체 생성
        MyProfileMateInfoDTO myProfileMateInfoDTO = new MyProfileMateInfoDTO();

        //  룸 메이트
        RoomMateProfile roomMateProfile = roomMateProfileRepository.findByUserId(user.getId()).orElse(null);

        if (roomMateProfile == null) {
            myProfileMateInfoDTO.setRoomMateStateMessage("룸 메이트 프로필이 작성되지 않았습니다.");
        } else {
            // Entity 값을 Dto 로 변환 받아서 저장
            RoomMateProfileInputDTO roomMateProfileInputDTO = RoomMateProfileInputDTO.from(roomMateProfile);
            myProfileMateInfoDTO.setRoomMateProfile(roomMateProfileInputDTO);
        }

        // 산책 메이트
        WalkMateProfile walkMateProfile = walkMateProfileRepository.findByUserId(user.getId()).orElse(null);

        if (walkMateProfile == null) {
            myProfileMateInfoDTO.setWalkMateStateMessage("산책 메이트 프로필이 작성되지 않았습니다.");
        } else {
            WalkMateProfileInputDTO walkMateProfileDTO = WalkMateProfileInputDTO.from(walkMateProfile);
            myProfileMateInfoDTO.setWalkMateProfile(walkMateProfileDTO);
        }

        // 식사 메이트
        FoodMateProfile foodMateProfile = foodMateProfileRepository.findByUserId(user.getId()).orElse(null);

        if (foodMateProfile == null) {
            myProfileMateInfoDTO.setFoodMateStateMessage("식사 메이트 프로필이 작성되지 않았습니다.");
        } else {
            FoodMateProfileInputDTO foodMateProfileInputDTO = FoodMateProfileInputDTO.from(foodMateProfile);
            myProfileMateInfoDTO.setFoodMateProfile(foodMateProfileInputDTO);
        }

        // 운동 메이트
        WorkoutMateProfile workoutMateProfile = workoutMateProfileRepository.findByUserId(user.getId()).orElse(null);

        if (workoutMateProfile == null) {
            myProfileMateInfoDTO.setWorkoutMateStateMessage("운동 메이트 프로필이 작성되지 않았습니다.");
        } else {
            WorkoutMateProfileInputDTO workoutMateProfileDTO = WorkoutMateProfileInputDTO.from(workoutMateProfile);
            myProfileMateInfoDTO.setWorkoutMateProfile(workoutMateProfileDTO);
        }

        // 스터디 메이트
        StudyMateProfile studyMateProfile = studyMateProfileRepository.findByUserId(user.getId()).orElse(null);

        if (studyMateProfile == null) {
            myProfileMateInfoDTO.setStudyMateStateMessage("스터디 메이트 프로필이 작성되지 않았습니다.");
        } else {
            StudyMateProfileInputDTO studyMateProfileDTO = StudyMateProfileInputDTO.from(studyMateProfile);
            myProfileMateInfoDTO.setStudyMateProfile(studyMateProfileDTO);
        }

        return myProfileMateInfoDTO;
    }


    /**
     * 유저 프로필 이미지 조회 로그인한 유저의 프로필 이미지를 조회합니다.
     *
     * @param customUserDetails
     * @return
     */
    @Transactional(readOnly = true)
    public String readProfileImgUrl(CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getId();

        // 40201 error
        MyProfile myProfile = myProfileRepository.findByUserId(userId).orElseThrow(InvalidTokenUser::new);

        String profileImg = myProfile.getProfileImgUrl();

        if (profileImg != null) {
            return profileImg;
        } else {
            return "프로필 이미지가 존재하지 않습니다.";
        }
    }

    /**
     * 입력 값이 모두 비어있는지 확인합니다.
     *
     * @param loginInfoDTO 입력된 사용자 정보
     * @param file         업로드된 파일
     * @return 입력 값이 유효하지 않으면 true
     */
    private boolean isInvalidInput(LoginInfoDTO loginInfoDTO, MultipartFile file) {
        // 로그인 정보와 파일이 모두 비어있을 경우 true 반환
        return (loginInfoDTO == null || (loginInfoDTO.getUsername() == null && loginInfoDTO.getPassword() == null))
                && (file == null || file.isEmpty());
    }
}
