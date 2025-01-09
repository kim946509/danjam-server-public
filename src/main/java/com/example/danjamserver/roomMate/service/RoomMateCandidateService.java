package com.example.danjamserver.roomMate.service;

import com.example.danjamserver.mate.util.MateFilterValidationService;
import com.example.danjamserver.roomMate.dto.RoomMateCandidateDetailDTO;
import com.example.danjamserver.roomMate.dto.RoomMateFilteringDTO;
import com.example.danjamserver.roomMate.dto.RoomMateProfileDTO;
import com.example.danjamserver.roomMate.enums.ActivityTime;
import com.example.danjamserver.roomMate.enums.CleanPeriod;
import com.example.danjamserver.roomMate.enums.Level;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.BlockRepository;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.CustomValidationException;
import com.example.danjamserver.util.exception.ResultCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.danjamserver.mate.util.MateFilterUtilService.convertoYear4Digits;
import static com.example.danjamserver.mate.util.MateFilterUtilService.fixFilterlingMBTI;

@Service
@RequiredArgsConstructor
public class RoomMateCandidateService {
    private final RoomMateFilteringService roomMateFilteringService;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    public List<RoomMateCandidateDetailDTO> getRoomMateCandidateList(CustomUserDetails customUserDetails, RoomMateFilteringDTO roomMateFilteringDTO) {

        User user = customUserDetails.getUser();
        Long schoolId = user.getSchool().getId();
        Integer gender = user.getGender();

        //Validation 추가
        String validationMessage = validateRoomMateFilteringDTO(roomMateFilteringDTO);
        if (!validationMessage.isEmpty()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, validationMessage);
        }

        String fixMbti = fixFilterlingMBTI(roomMateFilteringDTO.getMbti());// MBTI 필터링에 맞게 수정
        roomMateFilteringDTO.setMbti(fixMbti);

        // MinBirthYear 필터링을 년도 4자리로 변환
        if (roomMateFilteringDTO.getMinBirthYear() != null) {
            String min = convertoYear4Digits(roomMateFilteringDTO.getMinBirthYear());
            roomMateFilteringDTO.setMinBirthYear(min);
        }

        // MaxBirthYear 필터링을 년도 4자리로 변환
        if (roomMateFilteringDTO.getMaxBirthYear() != null) {
            String max = convertoYear4Digits(roomMateFilteringDTO.getMaxBirthYear());
            roomMateFilteringDTO.setMaxBirthYear(max);
        }

        // MinEntryYear 필터링을 년도 4자리로 변환
        if (roomMateFilteringDTO.getMinEntryYear() != null) {
            roomMateFilteringDTO.setMinEntryYear(convertoYear4Digits(roomMateFilteringDTO.getMinEntryYear()));
        }

        // MaxEntryYear 필터링을 년도 4자리로 변환
        if (roomMateFilteringDTO.getMaxEntryYear() != null) {
            roomMateFilteringDTO.setMaxEntryYear(convertoYear4Digits(roomMateFilteringDTO.getMaxEntryYear()));
        }

        // 흡연 여부 필터링을 위한 사전 작업
        Boolean isSmoking = null;
        if (roomMateFilteringDTO.getIsSmoking() != null && roomMateFilteringDTO.getIsSmoking().size() == 1) {
            isSmoking = roomMateFilteringDTO.getIsSmoking().iterator().next();
        }

        //상관 없어요는 null로 처리
        if (roomMateFilteringDTO.getHotLevel() == Level.NO_MATTER) {
            roomMateFilteringDTO.setHotLevel(null);
        }
        if (roomMateFilteringDTO.getColdLevel() == Level.NO_MATTER) {
            roomMateFilteringDTO.setColdLevel(null);
        }
        if (roomMateFilteringDTO.getActivityTime() == ActivityTime.NO_MATTER) {
            roomMateFilteringDTO.setActivityTime(null);
        }
        if (roomMateFilteringDTO.getCleanPeriod() == CleanPeriod.NO_MATTER) {
            roomMateFilteringDTO.setCleanPeriod(null);
        }

        // 학교, 성별, 최소&최대 생년월일, 최소&최대 입학년도, 흡연여부, 더위정도, 추위정도, 활동시간대, 청소주기, 수면습관, 샤워시간으로 후보자 필터링
        List<RoomMateProfileDTO> profilesByFilters = roomMateFilteringService.getFilteredCandidates(roomMateFilteringDTO, schoolId, gender, isSmoking);


        //profilesByFilters데이터에서 랜덤으로 20개의 데이터를 가져옴
        int limit = 20; // 2차로 선택할 후보자 수
        //20개 이상일 경우 랜덤으로 20개를 선택, 20개 이하일 경우 그대로 사용
        if (profilesByFilters.size() > limit) {
            Random random = new Random();
            int startRandomIndex = random.nextInt(profilesByFilters.size() - limit);
            profilesByFilters = profilesByFilters.subList(startRandomIndex, startRandomIndex + limit);
        }
        List<RoomMateCandidateDetailDTO> roomMateCandidateDetailDTOList = getRoomMateCandidateDetailDTOList(profilesByFilters);

        // 본인 제외.
        roomMateCandidateDetailDTOList.removeIf(roomMateCandidateDetailDTO -> roomMateCandidateDetailDTO.getNickname().equals(user.getNickname()));
        // 자신이 차단한 유저 제외.
        blockRepository.findBlockedUsersByBlocker(user).forEach(blockedUser -> roomMateCandidateDetailDTOList.removeIf(roomMateCandidateDetailDTO -> roomMateCandidateDetailDTO.getNickname().equals(blockedUser.getNickname())));

        //roomMateCandidateDetailDTOList의 순서를 랜덤으로 섞음. 반복된 데이터여도 새로운 데이터처럼 보이게 하기 위함
        Collections.shuffle(roomMateCandidateDetailDTOList);
        return roomMateCandidateDetailDTOList;
    }

    // 클라이언트에게 제공할 후보자 상세 정보를 반환
    // 20개의 후보자 정보를 반환
    private List<RoomMateCandidateDetailDTO> getRoomMateCandidateDetailDTOList(List<RoomMateProfileDTO> roomMateProfileDTOList) {
        List<Long> userIds = roomMateProfileDTOList.stream().map(RoomMateProfileDTO::getUserId).toList();// 후보자들의 userId
        List<User> userList = userRepository.findUsersByIds(userIds); // userId로 후보자들의 User 정보 조회(myProfile 정보도 함께 조회)
        List<RoomMateCandidateDetailDTO> roomMateCandidateDetailDTOList = new ArrayList<>();
        for (int i = 0; i < roomMateProfileDTOList.size(); i++) {
            User user = userList.get(i);
            MyProfile myProfile = user.getMyProfile();
            RoomMateProfileDTO roomMateProfile = roomMateProfileDTOList.get(i);
            RoomMateCandidateDetailDTO roomMateCandidateDetailDTO = RoomMateCandidateDetailDTO.builder()
                    .nickname(user.getNickname())
                    .major(myProfile.getMajor())
                    .mbti(myProfile.getMbti())
                    .greeting(myProfile.getGreeting())
                    .entryYear(myProfile.getEntryYear())
                    .birth(myProfile.getBirth())
                    .profileImgUrl(myProfile.getProfileImgUrl())
                    .hopeDormitories(roomMateProfile.getHopeDormitories())
                    .hopeRoomPersons(roomMateProfile.getHopeRoomPersons())
                    .sleepHabits(roomMateProfile.getOwnSleepHabits())
                    .isSmoking(roomMateProfile.getIsSmoking())
                    .hotLevel(roomMateProfile.getHotLevel())
                    .coldLevel(roomMateProfile.getColdLevel())
                    .activityTime(roomMateProfile.getActivityTime())
                    .cleanPeriod(roomMateProfile.getCleanPeriod())
                    .showerTime(roomMateProfile.getShowerTime())
                    .build();
            roomMateCandidateDetailDTOList.add(roomMateCandidateDetailDTO);
        }
        return roomMateCandidateDetailDTOList;
    }

    //Validation 추가
    private String validateRoomMateFilteringDTO(RoomMateFilteringDTO roomMateFilteringDTO) {
        StringBuilder errorMessage = new StringBuilder();

        MateFilterValidationService.validateMbti(roomMateFilteringDTO.getMbti(), errorMessage);

        MateFilterValidationService.validateBirthYear(roomMateFilteringDTO.getMinBirthYear(), roomMateFilteringDTO.getMaxBirthYear(), errorMessage);

        MateFilterValidationService.validateEntryYear(roomMateFilteringDTO.getMinEntryYear(), roomMateFilteringDTO.getMaxEntryYear(), errorMessage);

        validateSleepHabits(roomMateFilteringDTO.getSleepHabits(), errorMessage);
        return errorMessage.toString();
    }

    public void validateSleepHabits(Integer sleepHabits, StringBuilder errorMessage) {
        if (sleepHabits != null) {
            if (sleepHabits < 1 || sleepHabits > 4) {
                errorMessage.append("수면습관은 1~4까지 입력해주세요. ");
            }
        }
    }
}
