package com.example.danjamserver.workoutMate.service;

import com.example.danjamserver.mate.util.MateFilterValidationService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.BlockRepository;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.CustomValidationException;
import com.example.danjamserver.util.exception.ResultCode;
import com.example.danjamserver.workoutMate.dto.WorkoutMateFilteringDTO;
import com.example.danjamserver.workoutMate.dto.WorkoutMateCandiateDetailDTO;
import com.example.danjamserver.workoutMate.dto.WorkoutMateProfileDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.danjamserver.mate.util.MateFilterUtilService.convertoYear4Digits;
import static com.example.danjamserver.mate.util.MateFilterUtilService.fixFilterlingMBTI;


@Service
@RequiredArgsConstructor
public class WorkoutMateCandidateService {

    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final WorkoutMateFilteringService workoutMateFilteringService;

    @Transactional(readOnly = true)
    public Set<WorkoutMateCandiateDetailDTO> getWorkoutMateCandidateList(CustomUserDetails customUserDetails, WorkoutMateFilteringDTO workoutMateFilteringDTO) {

        User user = customUserDetails.getUser();
        Long schoolId = user.getSchool().getId();

        // validation check for filtering
        String validationMessage = validateWokroutMateFilteringDTO(workoutMateFilteringDTO);
        if (!validationMessage.isEmpty()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, validationMessage);
        }

        // 필터링을 위한 사전 작업
        workoutMateFilteringDTO.setMbti(fixFilterlingMBTI(workoutMateFilteringDTO.getMbti()));

        // MinBirthYear 필터링을 년도 4자리로 변환
        if (workoutMateFilteringDTO.getMinBirthYear() != null) {
            String min = convertoYear4Digits(workoutMateFilteringDTO.getMinBirthYear());
            workoutMateFilteringDTO.setMinBirthYear(min);
        }

        // MaxBirthYear 필터링을 년도 4자리로 변환
        if (workoutMateFilteringDTO.getMaxBirthYear() != null) {
            String max = convertoYear4Digits(workoutMateFilteringDTO.getMaxBirthYear());
            workoutMateFilteringDTO.setMaxBirthYear(max);
        }

        // MinEntryYear 필터링을 년도 4자리로 변환
        if (workoutMateFilteringDTO.getMinEntryYear() != null) {
            workoutMateFilteringDTO.setMinEntryYear(convertoYear4Digits(workoutMateFilteringDTO.getMinEntryYear()));
        }

        // MaxEntryYear 필터링을 년도 4자리로 변환
        if (workoutMateFilteringDTO.getMaxEntryYear() != null) {

            workoutMateFilteringDTO.setMaxEntryYear(convertoYear4Digits(workoutMateFilteringDTO.getMaxEntryYear()));
        }
        // List gender 필터링
        Integer filterGender = null;
        if (workoutMateFilteringDTO.getGender() != null) {
            if(workoutMateFilteringDTO.getGender().size() == 1)
                filterGender = workoutMateFilteringDTO.getGender().iterator().next();
        }

        //필터링
        List<WorkoutMateProfileDTO> profilesByFilters = workoutMateFilteringService.getFilteredCandidates(workoutMateFilteringDTO, schoolId, filterGender);
        int limit = 20; // 2차로 선택할 후보자 수
        //20개 이상일 경우 랜덤으로 20개를 선택, 20개 이하일 경우 그대로 사용
        if(profilesByFilters.size()>limit){
            Random random = new Random();
            int randomStartIndex = random.nextInt(profilesByFilters.size() - limit);
            profilesByFilters = profilesByFilters.subList(randomStartIndex, randomStartIndex + limit);
        }

        //상세 정보 조회
        List<WorkoutMateCandiateDetailDTO> workoutMateCandiateDetailDTOList = getWorkoutMateCandidateDetailList(profilesByFilters);

        // 본인 제외
        workoutMateCandiateDetailDTOList.removeIf(workoutMateCandidateDetailDTO -> workoutMateCandidateDetailDTO.getNickname().equals(user.getNickname()));

        // 차단 목록 제거
        blockRepository.findBlockedUsersByBlocker(user).forEach(blockedUser -> workoutMateCandiateDetailDTOList.removeIf(workoutMateCandidateDetailDTO -> workoutMateCandidateDetailDTO.getNickname().equals(blockedUser.getNickname())));

        // 결과 섞기
        Collections.shuffle(workoutMateCandiateDetailDTOList);

        return new HashSet<>(workoutMateCandiateDetailDTOList);
    }

    // 후보자 상세 정보 조회 메서드
    private List<WorkoutMateCandiateDetailDTO> getWorkoutMateCandidateDetailList(List<WorkoutMateProfileDTO> workoutMateProfileDTOList) {
        List<Long> userIds = workoutMateProfileDTOList.stream().map(WorkoutMateProfileDTO::getUserId).toList();// 후보자들의 userId
        List<User> userList = userRepository.findUsersByIds(userIds); // userId로 후보자들의 User 정보 조회(myProfile 정보도 함께 조회)

        List<WorkoutMateCandiateDetailDTO> candidateDetailDTOList = new ArrayList<>();
        for (int i = 0; i < workoutMateProfileDTOList.size(); i++) {
            User user = userList.get(i);
            MyProfile myProfile = user.getMyProfile();
            WorkoutMateProfileDTO workoutMateProfileDTO = workoutMateProfileDTOList.get(i);
            WorkoutMateCandiateDetailDTO workoutMateCandiateDetailDTO = WorkoutMateCandiateDetailDTO.builder()
                    .nickname(user.getNickname())
                    .major(myProfile.getMajor())
                    .mbti(myProfile.getMbti())
                    .greeting(myProfile.getGreeting())
                    .entryYear(myProfile.getEntryYear())
                    .birth(myProfile.getBirth())
                    .gender(user.getGender())
                    .profileImgUrl(myProfile.getProfileImgUrl())
                    .preferredWorkoutIntensity(workoutMateProfileDTO.getPreferredWorkoutIntensity())
                    .preferredWorkoutTimeZones(workoutMateProfileDTO.getPreferredWorkoutTimeZones())
                    .preferredWorkoutTimes(workoutMateProfileDTO.getPreferredWorkoutTimes())
                    .preferredWorkoutTypes(workoutMateProfileDTO.getPreferredWorkoutTypes())
                    .build();
            candidateDetailDTOList.add(workoutMateCandiateDetailDTO);
        }
        return candidateDetailDTOList;
    }

    private String validateWokroutMateFilteringDTO(WorkoutMateFilteringDTO walkMateFilteringDTO) {

        StringBuilder errorMessage = new StringBuilder();

        MateFilterValidationService.validateMbti(walkMateFilteringDTO.getMbti(), errorMessage);

        MateFilterValidationService.validateBirthYear(walkMateFilteringDTO.getMinBirthYear(), walkMateFilteringDTO.getMaxBirthYear(), errorMessage);

        MateFilterValidationService.validateGender(walkMateFilteringDTO.getGender(), errorMessage);
        return errorMessage.toString();
    }
}
