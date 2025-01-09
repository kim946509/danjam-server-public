package com.example.danjamserver.workoutMate.service;

import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.MyProfileRepository;
import com.example.danjamserver.util.exception.InvalidRequestException;
import com.example.danjamserver.util.exception.ResultCode;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTime;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTimeZone;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutType;
import com.example.danjamserver.workoutMate.domain.WorkoutMateProfile;
import com.example.danjamserver.workoutMate.dto.WorkoutMateProfileInputDTO;
import com.example.danjamserver.workoutMate.enums.WorkoutTime;
import com.example.danjamserver.workoutMate.enums.WorkoutTimeZone;
import com.example.danjamserver.workoutMate.enums.WorkoutType;
import com.example.danjamserver.workoutMate.repository.PreferredWorkoutTimeRepository;
import com.example.danjamserver.workoutMate.repository.PreferredWorkoutTimeZoneRepository;
import com.example.danjamserver.workoutMate.repository.PreferredWorkoutTypeRepository;
import com.example.danjamserver.workoutMate.repository.WorkoutMateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class WorkoutMateProfileService {

    private final PreferredWorkoutTimeRepository preferredWorkoutTimeRepository;
    private final PreferredWorkoutTypeRepository preferredWorkoutTypeRepository;
    private final PreferredWorkoutTimeZoneRepository preferredWorkoutTimeZoneRepository;
    private final WorkoutMateProfileRepository workoutMateProfileRepository;
    private final MyProfileRepository myProfileRepository;

    @Transactional
    public void createWorkoutMateProfile(CustomUserDetails customUserDetails, WorkoutMateProfileInputDTO workoutMateProfileInputDTO) {

        User user = customUserDetails.getUser();

        //이미 MyProfile이 존재하는지 확인
        if(!isMyProfileAlreadySetting(user)){
            throw new InvalidRequestException(ResultCode.MYPROFILE_REQUIRED);
        }

        //이미 WorkoutMateProfile이 존재하는지 확인
        WorkoutMateProfile workoutMateProfileCheck = workoutMateProfileRepository.findByUserId(user.getId()).orElse(null);
        if(workoutMateProfileCheck != null) {
            throw new InvalidRequestException(ResultCode.ALREADY_EXIST_PROFILE);
        }

        // workoutMateProfileInputDTO의 정보를 WorkoutMateProfile에 변환하여 저장
        WorkoutMateProfile workoutMateProfile = new WorkoutMateProfile();

        // 선호하는 운동 강도 저장
        workoutMateProfile.setPreferredWorkoutIntensity(workoutMateProfileInputDTO.getPreferredWorkoutIntensity());
        workoutMateProfile.setUser(user);
        workoutMateProfile.setMateType(MateType.WORKOUTMATE);
        workoutMateProfileRepository.save(workoutMateProfile);

        // preferredWorkoutTime 저장
        Set<WorkoutTime> preferredWorkoutTimes = workoutMateProfileInputDTO.getPreferredWorkoutTimes();
        for(WorkoutTime preferredWorkoutTime : preferredWorkoutTimes) {
            PreferredWorkoutTime preferredWorkoutTimeEntity = PreferredWorkoutTime.builder()
                    .workoutMateProfile(workoutMateProfile)
                    .workoutTime(preferredWorkoutTime)
                    .build();
            preferredWorkoutTimeRepository.save(preferredWorkoutTimeEntity);
        }

        // preferredWorkoutType 저장
        Set<WorkoutType> preferredWorkoutTypes = workoutMateProfileInputDTO.getPreferredWorkoutTypes();

        // WorkoutType.ANYWAY가 포함되어 있을 경우, WorkoutType.ANYWAY만 저장
        if(preferredWorkoutTypes.contains(WorkoutType.ANYWAY)){
            PreferredWorkoutType preferredWorkoutTypeEntity = PreferredWorkoutType.builder()
                    .workoutMateProfile(workoutMateProfile)
                    .workoutType(WorkoutType.ANYWAY)
                    .build();
            preferredWorkoutTypeRepository.save(preferredWorkoutTypeEntity);
        }
        // WorkoutType.ANYWAY가 포함되어 있지 않을 경우, 모든 WorkoutType 저장
        else {
            for (WorkoutType preferredWorkoutType : preferredWorkoutTypes) {
                PreferredWorkoutType preferredWorkoutTypeEntity = PreferredWorkoutType.builder()
                        .workoutMateProfile(workoutMateProfile)
                        .workoutType(preferredWorkoutType)
                        .build();
                preferredWorkoutTypeRepository.save(preferredWorkoutTypeEntity);
            }
        }

        // preferredWorkoutTimeZone 저장
        Set<WorkoutTimeZone> preferredWorkoutTimeZones = workoutMateProfileInputDTO.getPreferredWorkoutTimeZones();
        for(WorkoutTimeZone preferredWorkoutTimeZone : preferredWorkoutTimeZones) {
            PreferredWorkoutTimeZone preferredWorkoutTimeZoneEntity = PreferredWorkoutTimeZone.builder()
                    .workoutMateProfile(workoutMateProfile)
                    .workoutTimeZone(preferredWorkoutTimeZone)
                    .build();
            preferredWorkoutTimeZoneRepository.save(preferredWorkoutTimeZoneEntity);
        }
    }

    public boolean isMyProfileAlreadySetting(User user){
        boolean isAlreadySetting = true;
        MyProfile myProfile = myProfileRepository.findByUserId(user.getId()).orElse(null);
        if(myProfile == null || myProfile.getMbti() == null){
            isAlreadySetting = false;
        }
        return isAlreadySetting;
    }
}
