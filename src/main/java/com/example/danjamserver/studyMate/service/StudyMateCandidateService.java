package com.example.danjamserver.studyMate.service;

import static com.example.danjamserver.mate.util.MateFilterUtilService.convertoYear4Digits;
import static com.example.danjamserver.mate.util.MateFilterUtilService.fixFilterlingMBTI;

import com.example.danjamserver.mate.util.MateFilterValidationService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.studyMate.dto.StudyMateCandidateDetailDTO;
import com.example.danjamserver.studyMate.dto.StudyMateFilteringDTO;
import com.example.danjamserver.studyMate.dto.StudyMateProfileDTO;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.BlockRepository;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.CustomValidationException;
import com.example.danjamserver.util.exception.ResultCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyMateCandidateService {

    private final StudyMateFilteringService studyMateFilteringService;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    @Transactional
    public Set<StudyMateCandidateDetailDTO> getStudyMateCandidates(CustomUserDetails customUserDetails,
                                                                   StudyMateFilteringDTO studyMateFilteringDTO) {

        User user = customUserDetails.getUser();
        Long schoolId = user.getSchool().getId();

        //Validation
        String validationMessage = validateStudyMateFilteringDTO(studyMateFilteringDTO);
        if (!validationMessage.isEmpty()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, validationMessage);
        }

        // 필터링을 위한 사전 작업
        studyMateFilteringDTO.setMbti(fixFilterlingMBTI(studyMateFilteringDTO.getMbti()));

        // MinBirthYear 필터링을 년도 4자리로 변환
        if (studyMateFilteringDTO.getMinBirthYear() != null) {
            String min = convertoYear4Digits(studyMateFilteringDTO.getMinBirthYear());
            studyMateFilteringDTO.setMinBirthYear(min);
        }

        // MaxBirthYear 필터링을 년도 4자리로 변환
        if (studyMateFilteringDTO.getMaxBirthYear() != null) {
            String max = convertoYear4Digits(studyMateFilteringDTO.getMaxBirthYear());
            studyMateFilteringDTO.setMaxBirthYear(max);
        }

        // MinEntryYear 필터링을 년도 4자리로 변환
        if (studyMateFilteringDTO.getMinEntryYear() != null) {
            studyMateFilteringDTO.setMinEntryYear(convertoYear4Digits(studyMateFilteringDTO.getMinEntryYear()));
        }

        // MaxEntryYear 필터링을 년도 4자리로 변환
        if (studyMateFilteringDTO.getMaxEntryYear() != null) {
            studyMateFilteringDTO.setMaxEntryYear(convertoYear4Digits(studyMateFilteringDTO.getMaxEntryYear()));
        }

        // List gender 필터링
        Integer genderFilter = null;
        if (studyMateFilteringDTO.getGender() != null) {
            if (studyMateFilteringDTO.getGender().size() == 1) {
                genderFilter = studyMateFilteringDTO.getGender().iterator().next();
            }
        }

        // 후보자 필터링
        List<StudyMateProfileDTO> profilesByFilters = studyMateFilteringService.getFilteredCandidates(
                studyMateFilteringDTO, schoolId, genderFilter);
        int limit = 20; // 2차로 선택할 후보자 수
        //20개 이상일 경우 랜덤으로 20개를 선택, 20개 이하일 경우 그대로 사용
        if (profilesByFilters.size() > limit) {
            Random random = new Random();
            int randomStartIndex = random.nextInt(profilesByFilters.size() - limit);
            profilesByFilters = profilesByFilters.subList(randomStartIndex, randomStartIndex + limit);
        }

        // 후보자 상세 정보 조회
        List<StudyMateCandidateDetailDTO> studyMateCandidateDetailDTOList = getStudyMateCandidateDetailList(
                profilesByFilters);

        // 본인 제외
        studyMateCandidateDetailDTOList.removeIf(
                studyMateCandidateDetailDTO -> studyMateCandidateDetailDTO.getNickname().equals(user.getNickname()));

        // 차단 목록 제거
        blockRepository.findBlockedUsersByBlocker(user).forEach(blockedUser -> studyMateCandidateDetailDTOList.removeIf(
                studyMateCandidateDetailDTO -> studyMateCandidateDetailDTO.getNickname()
                        .equals(blockedUser.getNickname())));

        // 결과 섞기
        Collections.shuffle(studyMateCandidateDetailDTOList);

        return new HashSet<>(studyMateCandidateDetailDTOList);
    }

    // 클라이언트에게 제공할 후보자들의 모든 정보를 담은 DTO를 반환
    // 20명의 후보자들의 StudyMateCandidateDetailDTO를 반환
    private List<StudyMateCandidateDetailDTO> getStudyMateCandidateDetailList(
            List<StudyMateProfileDTO> studyMateProfileDTOList) {
        List<Long> userIds = studyMateProfileDTOList.stream().map(StudyMateProfileDTO::getUserId)
                .toList();// 후보자들의 userId
        List<User> userList = userRepository.findUsersByIds(userIds); // userId로 후보자들의 User 정보 조회(myProfile 정보도 함께 조회)
        List<StudyMateCandidateDetailDTO> candidateDetailDTOList = new ArrayList<>();
        for (int i = 0; i < studyMateProfileDTOList.size(); i++) {
            User user = userList.get(i);
            MyProfile myProfile = user.getMyProfile();
            StudyMateProfileDTO studyMateProfileDTO = studyMateProfileDTOList.get(i);
            StudyMateCandidateDetailDTO studyMateCandidateDetailDTO = StudyMateCandidateDetailDTO.builder()
                    .nickname(user.getNickname())
                    .major(myProfile.getMajor())
                    .mbti(myProfile.getMbti())
                    .greeting(myProfile.getGreeting())
                    .entryYear(myProfile.getEntryYear())
                    .birth(myProfile.getBirth())
                    .gender(user.getGender())
                    .profileImgUrl(myProfile.getProfileImgUrl())
                    .preferredStudyTime(studyMateProfileDTO.getPreferredStudyTime())
                    .preferredStudyTypes(studyMateProfileDTO.getPreferredStudyTypes())
                    .subjects(studyMateProfileDTO.getUserSubjects())
                    .averageGrade(studyMateProfileDTO.getAverageGrade())
                    .build();
            candidateDetailDTOList.add(studyMateCandidateDetailDTO);
        }
        return candidateDetailDTOList;
    }

    private String validateStudyMateFilteringDTO(StudyMateFilteringDTO studyMateFilteringDTO) {

        StringBuilder errorMessage = new StringBuilder();

        MateFilterValidationService.validateMbti(studyMateFilteringDTO.getMbti(), errorMessage);

        MateFilterValidationService.validateBirthYear(studyMateFilteringDTO.getMinBirthYear(),
                studyMateFilteringDTO.getMaxBirthYear(), errorMessage);

        MateFilterValidationService.validateEntryYear(studyMateFilteringDTO.getMinEntryYear(),
                studyMateFilteringDTO.getMaxEntryYear(), errorMessage);

        MateFilterValidationService.validateGender(studyMateFilteringDTO.getGender(), errorMessage);
        return errorMessage.toString();
    }

}
