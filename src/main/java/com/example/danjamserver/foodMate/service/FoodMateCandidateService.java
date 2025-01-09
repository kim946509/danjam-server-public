package com.example.danjamserver.foodMate.service;

import static com.example.danjamserver.mate.util.MateFilterUtilService.convertoYear4Digits;
import static com.example.danjamserver.mate.util.MateFilterUtilService.fixFilterlingMBTI;

import com.example.danjamserver.foodMate.dto.FoodMateCandidateDetailDTO;
import com.example.danjamserver.foodMate.dto.FoodMateFilteringDTO;
import com.example.danjamserver.foodMate.dto.FoodMateProfileDTO;
import com.example.danjamserver.mate.util.MateFilterValidationService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.BlockRepository;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.CustomValidationException;
import com.example.danjamserver.util.exception.ResultCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodMateCandidateService {

    private final UserRepository userRepository;
    private final FoodMateFilteringService foodMateFilteringService;
    private final BlockRepository blockRepository;

    public Set<FoodMateCandidateDetailDTO> getFoodMateCandidateList(CustomUserDetails customUserDetails,
                                                                    FoodMateFilteringDTO foodMateFilteringDto) {

        User user = customUserDetails.getUser();
        Long schoolId = user.getSchool().getId();

        //Validation
        String validationMessage = validateFoodMateFilteringDTO(foodMateFilteringDto);
        if (!validationMessage.isEmpty()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, validationMessage);
        }

        // 필터링을 위한 사전 작업
        // MBTI 필터링 값을 필터링에 맞게 변경
        foodMateFilteringDto.setMbti(fixFilterlingMBTI(foodMateFilteringDto.getMbti()));

        // MinBirthYear 필터링을 년도 4자리로 변환
        if (foodMateFilteringDto.getMinBirthYear() != null) {
            String min = convertoYear4Digits(foodMateFilteringDto.getMinBirthYear());
            foodMateFilteringDto.setMinBirthYear(min);
        }

        // MaxBirthYear 필터링을 년도 4자리로 변환
        if (foodMateFilteringDto.getMaxBirthYear() != null) {
            String max = convertoYear4Digits(foodMateFilteringDto.getMaxBirthYear());
            foodMateFilteringDto.setMaxBirthYear(max);
        }

        // MinEntryYear 필터링을 년도 4자리로 변환
        if (foodMateFilteringDto.getMinEntryYear() != null) {
            foodMateFilteringDto.setMinEntryYear(convertoYear4Digits(foodMateFilteringDto.getMinEntryYear()));
        }

        // MaxEntryYear 필터링을 년도 4자리로 변환
        if (foodMateFilteringDto.getMaxEntryYear() != null) {
            foodMateFilteringDto.setMaxEntryYear(convertoYear4Digits(foodMateFilteringDto.getMaxEntryYear()));
        }

        // List gender 필터링
        Integer genderFilter = null;
        if (foodMateFilteringDto.getGender() != null) {
            if (foodMateFilteringDto.getGender().size() == 1) {
                genderFilter = foodMateFilteringDto.getGender().iterator().next();
            }
        }

        // 후보자 리스트 조회
        List<FoodMateProfileDTO> profilesByFilters = foodMateFilteringService.getFilteredCandidates(
                foodMateFilteringDto, schoolId, genderFilter);

        int limit = 20; // 2차로 선택할 후보자 수
        //20개 이상일 경우 랜덤으로 20개를 선택, 20개 이하일 경우 그대로 사용
        if (profilesByFilters.size() > limit) {
            Random random = new Random();
            int randomStartIndex = random.nextInt(profilesByFilters.size() - limit);
            profilesByFilters = profilesByFilters.subList(randomStartIndex, randomStartIndex + limit);
        }

        // 후보자 상세 정보 조회
        List<FoodMateCandidateDetailDTO> foodMateCandidateDetailDTOList = getFoodMateCandidateDetailList(
                profilesByFilters);

        // 본인 제외
        foodMateCandidateDetailDTOList.removeIf(
                foodMateCandidateDetailDTO -> foodMateCandidateDetailDTO.getNickname().equals(user.getNickname()));

        // 차단 목록 제거
        blockRepository.findBlockedUsersByBlocker(user).forEach(blockedUser -> foodMateCandidateDetailDTOList.removeIf(
                foodMateCandidateDetailDTO -> foodMateCandidateDetailDTO.getNickname()
                        .equals(blockedUser.getNickname())));

        // 결과 섞기
        Collections.shuffle(foodMateCandidateDetailDTOList);

        return Set.copyOf(foodMateCandidateDetailDTOList);
    }

    // 후보자 상세 정보 조회
    private List<FoodMateCandidateDetailDTO> getFoodMateCandidateDetailList(
            List<FoodMateProfileDTO> foodMateProfileDTOList) {
        List<Long> userIds = foodMateProfileDTOList.stream().map(FoodMateProfileDTO::getUserId).toList();// 후보자들의 userId
        List<User> userList = userRepository.findUsersByIds(userIds); // userId로 후보자들의 User 정보 조회(myProfile 정보도 함께 조회)
        List<FoodMateCandidateDetailDTO> candidateDetailDTOList = new ArrayList<>();
        for (int i = 0; i < foodMateProfileDTOList.size(); i++) {
            User user = userList.get(i);
            MyProfile myProfile = user.getMyProfile();
            FoodMateProfileDTO foodMateProfileDTO = foodMateProfileDTOList.get(i);
            FoodMateCandidateDetailDTO foodMateCandidateDetailDTO = FoodMateCandidateDetailDTO.builder()
                    .nickname(user.getNickname())
                    .major(myProfile.getMajor())
                    .mbti(myProfile.getMbti())
                    .greeting(myProfile.getGreeting())
                    .entryYear(myProfile.getEntryYear())
                    .birth(myProfile.getBirth())
                    .gender(user.getGender())
                    .profileImgUrl(myProfile.getProfileImgUrl())
                    .mateTime(foodMateProfileDTO.getMateTime())
                    .menu(foodMateProfileDTO.getHopeMenu())
                    .mealTime(foodMateProfileDTO.getMealTime())
                    .diningManner(foodMateProfileDTO.getDiningManner())
                    .allergies(foodMateProfileDTO.getAllergies())
                    .build();
            candidateDetailDTOList.add(foodMateCandidateDetailDTO);
        }
        return candidateDetailDTOList;
    }

    private String validateFoodMateFilteringDTO(FoodMateFilteringDTO foodMateFilteringDto) {

        StringBuilder errorMessage = new StringBuilder();

        MateFilterValidationService.validateMbti(foodMateFilteringDto.getMbti(), errorMessage);

        MateFilterValidationService.validateBirthYear(foodMateFilteringDto.getMinBirthYear(),
                foodMateFilteringDto.getMaxBirthYear(), errorMessage);

        MateFilterValidationService.validateEntryYear(foodMateFilteringDto.getMinEntryYear(),
                foodMateFilteringDto.getMaxEntryYear(), errorMessage);

        MateFilterValidationService.validateGender(foodMateFilteringDto.getGender(), errorMessage);
        return errorMessage.toString();
    }

}
