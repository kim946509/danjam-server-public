package com.example.danjamserver.walkMate.service;

import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.MyProfileRepository;
import com.example.danjamserver.util.exception.InvalidRequestException;
import com.example.danjamserver.util.exception.ResultCode;
import com.example.danjamserver.walkMate.domain.PreferredWalkIntensity;
import com.example.danjamserver.walkMate.domain.PreferredWalkTimeZone;
import com.example.danjamserver.walkMate.domain.WalkMateProfile;
import com.example.danjamserver.walkMate.dto.WalkMateProfileInputDTO;
import com.example.danjamserver.walkMate.enums.WalkIntensity;
import com.example.danjamserver.walkMate.enums.WalkTimeZone;
import com.example.danjamserver.walkMate.repository.PreferredWalkIntensityRepository;
import com.example.danjamserver.walkMate.repository.PreferredWalkTimeZoneRepository;
import com.example.danjamserver.walkMate.repository.WalkMateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalkMateProfileService {

    private final WalkMateProfileRepository walkMateProfileRepository;
    private final PreferredWalkIntensityRepository preferredWalkIntensityRepository;
    private final PreferredWalkTimeZoneRepository preferredWalkTimeZoneRepository;
    private final MyProfileRepository myProfileRepository;

    @Transactional
    public void createWalkMateProfile(CustomUserDetails customUserDetails,
                                      WalkMateProfileInputDTO walkMateProfileInputDTO) {

        User user = customUserDetails.getUser();

        //이미 MyProfile이 존재하는지 확인
        if (!isMyProfileAlreadySetting(user)) {
            throw new InvalidRequestException(ResultCode.MYPROFILE_REQUIRED);
        }

        // 이미 WalkMateProfile이 존재하는지 확인
        WalkMateProfile walkMateProfileCheck = walkMateProfileRepository.findByUserId(user.getId()).orElse(null);
        if (walkMateProfileCheck != null) {
            throw new InvalidRequestException(ResultCode.ALREADY_EXIST_PROFILE);
        }

        // walkMateProfileInputDTO의 정보를 WalkMateProfile에 변환하여 저장
        WalkMateProfile walkMateProfile = new WalkMateProfile();

        // 선호하는 걷기 시간 저장
        walkMateProfile.setPreferredWalkTime(walkMateProfileInputDTO.getPreferredWalkTime());

        // 유저 정보 저장
        walkMateProfile.setUser(user);

        // MateType 저장
        walkMateProfile.setMateType(MateType.WALKMATE);
        walkMateProfileRepository.save(walkMateProfile);

        // 선호 시간대 저장
        for (WalkTimeZone walkTimeZone : walkMateProfileInputDTO.getPreferredWalkTimeZones()) {
            preferredWalkTimeZoneRepository.save(PreferredWalkTimeZone.builder()
                    .walkMateProfile(walkMateProfile)
                    .walkTimeZone(walkTimeZone)
                    .build());
        }
        // 선호하는 걷기 강도 저장
        for (WalkIntensity walkIntensity : walkMateProfileInputDTO.getPreferredWalkIntensities()) {
            preferredWalkIntensityRepository.save(PreferredWalkIntensity.builder()
                    .walkMateProfile(walkMateProfile)
                    .walkIntensity(walkIntensity)
                    .build());
        }
    }

    public boolean isMyProfileAlreadySetting(User user) {
        boolean isAlreadySetting = true;
        MyProfile myProfile = myProfileRepository.findByUserId(user.getId()).orElse(null);
        if (myProfile == null || myProfile.getMbti() == null) {
            isAlreadySetting = false;
        }
        return isAlreadySetting;
    }
}
