package com.example.danjamserver.walkMate.service;

import static com.example.danjamserver.mate.util.MateFilterUtilService.convertoYear4Digits;
import static com.example.danjamserver.mate.util.MateFilterUtilService.fixFilterlingMBTI;

import com.example.danjamserver.mate.util.MateFilterValidationService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.BlockRepository;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.CustomValidationException;
import com.example.danjamserver.util.exception.ResultCode;
import com.example.danjamserver.walkMate.dto.WalkMateCandidateDetailDTO;
import com.example.danjamserver.walkMate.dto.WalkMateFilteringDTO;
import com.example.danjamserver.walkMate.dto.WalkMateProfileDTO;
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
public class WalkMateCandidateService {

    private final WalkMateFilteringService walkMateFilteringService;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    @Transactional
    public Set<WalkMateCandidateDetailDTO> getWalkMateCandidates(CustomUserDetails customUserDetails,
                                                                 WalkMateFilteringDTO walkMateFilteringDTO) {

        User user = customUserDetails.getUser();
        Long schoolId = user.getSchool().getId();

        // validation
        String validationMessage = validateWalkMateFilteringDTO(walkMateFilteringDTO);
        if (!validationMessage.isEmpty()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, validationMessage);
        }

        // 필터링을 위한 사전 작업
        // MBTI 필터링 수정(반대되는 mbti를 모두 가지고 있을경우 조건 제거)
        walkMateFilteringDTO.setMbti(fixFilterlingMBTI(walkMateFilteringDTO.getMbti()));

        // MinBirthYear 필터링을 년도 4자리로 변환
        if (walkMateFilteringDTO.getMinBirthYear() != null) {
            String min = convertoYear4Digits(walkMateFilteringDTO.getMinBirthYear());
            walkMateFilteringDTO.setMinBirthYear(min);
        }

        // MaxBirthYear 필터링을 년도 4자리로 변환
        if (walkMateFilteringDTO.getMaxBirthYear() != null) {
            String max = convertoYear4Digits(walkMateFilteringDTO.getMaxBirthYear());
            walkMateFilteringDTO.setMaxBirthYear(max);
        }

        // MinEntryYear 필터링을 년도 4자리로 변환
        if (walkMateFilteringDTO.getMinEntryYear() != null) {
            walkMateFilteringDTO.setMinEntryYear(convertoYear4Digits(walkMateFilteringDTO.getMinEntryYear()));
        }

        // MaxEntryYear 필터링을 년도 4자리로 변환
        if (walkMateFilteringDTO.getMaxEntryYear() != null) {

            walkMateFilteringDTO.setMaxEntryYear(convertoYear4Digits(walkMateFilteringDTO.getMaxEntryYear()));
        }

        // List gender 필터링
        Integer filterGender = null;
        if (walkMateFilteringDTO.getGender() != null) {
            if (walkMateFilteringDTO.getGender().size() == 1) {
                filterGender = walkMateFilteringDTO.getGender().iterator().next();
            }
        }

        // 산책 메이트 후보자 조회 (학교 정보, 성별, 생년월일, 입학년도, MBTI 필터링)
        List<WalkMateProfileDTO> profilesByFilters = walkMateFilteringService.getFilteredCandidates(
                walkMateFilteringDTO, schoolId, filterGender);
        int limit = 20; // 2차로 선택할 후보자 수
        //20개 이상일 경우 랜덤으로 20개를 선택, 20개 이하일 경우 그대로 사용
        if (profilesByFilters.size() > limit) {
            Random random = new Random();
            int randomStartIndex = random.nextInt(profilesByFilters.size() - limit);
            profilesByFilters = profilesByFilters.subList(randomStartIndex, randomStartIndex + limit);
        }

        //상세 정보 조회
        List<WalkMateCandidateDetailDTO> walkMateCandidateDetailDTOList = getWalkMateCandidateDetailList(
                profilesByFilters);

        // 본인 제외
        walkMateCandidateDetailDTOList.removeIf(
                walkMateCandidateDetailDTO -> walkMateCandidateDetailDTO.getNickname().equals(user.getNickname()));

        // 차단 목록 제거
        blockRepository.findBlockedUsersByBlocker(user).forEach(blockedUser -> walkMateCandidateDetailDTOList.removeIf(
                walkMateCandidateDetailDTO -> walkMateCandidateDetailDTO.getNickname()
                        .equals(blockedUser.getNickname())));

        // 결과 섞기
        Collections.shuffle(walkMateCandidateDetailDTOList);

        return new HashSet<>(walkMateCandidateDetailDTOList);
    }

    // 클라이언트에게 제공할 후보자들의 모든 정보를 담은 DTO를 반환
    // 20명의 후보자들의 StudyMateCandidateDetailDTO를 반환
    private List<WalkMateCandidateDetailDTO> getWalkMateCandidateDetailList(
            List<WalkMateProfileDTO> walkMateProfileDTOList) {
        List<Long> userIds = walkMateProfileDTOList.stream().map(WalkMateProfileDTO::getUserId).toList();// 후보자들의 userId
        List<User> userList = userRepository.findUsersByIds(userIds); // userId로 후보자들의 User 정보 조회(myProfile 정보도 함께 조회)

        List<WalkMateCandidateDetailDTO> candidateDetailDTOList = new ArrayList<>(); // 후보자들의 정보를 저장할 List
        for (int i = 0; i < walkMateProfileDTOList.size(); i++) {
            User user = userList.get(i);
            MyProfile myProfile = user.getMyProfile();
            WalkMateProfileDTO walkMateProfileDTO = walkMateProfileDTOList.get(i);
            WalkMateCandidateDetailDTO walkMateCandidateDetailDTO = WalkMateCandidateDetailDTO.builder()
                    .nickname(user.getNickname())
                    .major(myProfile.getMajor())
                    .mbti(myProfile.getMbti())
                    .greeting(myProfile.getGreeting())
                    .entryYear(myProfile.getEntryYear())
                    .birth(myProfile.getBirth())
                    .gender(user.getGender())
                    .profileImgUrl(myProfile.getProfileImgUrl())
                    .preferredWalkTime(walkMateProfileDTO.getPreferredWalkTime())
                    .preferredWalkIntensities(walkMateProfileDTO.getPreferredWalkIntensities())
                    .preferredWalkTimeZones(walkMateProfileDTO.getPreferredWalkTimeZones())
                    .build();
            candidateDetailDTOList.add(walkMateCandidateDetailDTO);
        }
        return candidateDetailDTOList;
    }

    private String validateWalkMateFilteringDTO(WalkMateFilteringDTO walkMateFilteringDTO) {

        StringBuilder errorMessage = new StringBuilder();

        MateFilterValidationService.validateMbti(walkMateFilteringDTO.getMbti(), errorMessage);

        MateFilterValidationService.validateBirthYear(walkMateFilteringDTO.getMinBirthYear(),
                walkMateFilteringDTO.getMaxBirthYear(), errorMessage);

        MateFilterValidationService.validateGender(walkMateFilteringDTO.getGender(), errorMessage);
        return errorMessage.toString();
    }

}
