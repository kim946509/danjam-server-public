package com.example.danjamserver.roomMate.service;

import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.roomMate.domain.HopeDormitory;
import com.example.danjamserver.roomMate.domain.HopeRoomPerson;
import com.example.danjamserver.roomMate.domain.OwnSleepHabit;
import com.example.danjamserver.roomMate.domain.RoomMateProfile;
import com.example.danjamserver.roomMate.enums.SleepHabit;
import com.example.danjamserver.roomMate.repository.HopeDormitoryRepository;
import com.example.danjamserver.roomMate.repository.HopeRoomPersonRepository;
import com.example.danjamserver.roomMate.repository.OwnSleepHabitRepository;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.MyProfileRepository;
import com.example.danjamserver.util.exception.InvalidRequestException;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import com.example.danjamserver.roomMate.dto.RoomMateProfileInputDTO;
import com.example.danjamserver.roomMate.repository.RoomMateProfileRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.exception.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomMateProfileService {
    private final RoomMateProfileRepository roomMateProfileRepository;
    private final MyProfileRepository myProfileRepository;
    private final HopeDormitoryRepository hopeDormitoryRepository;
    private final HopeRoomPersonRepository hopeRoomPersonRepository;
    private final OwnSleepHabitRepository ownSleepHabitRepository;

    @Transactional
    public void createRoomMateProfile(RoomMateProfileInputDTO roomMateProfileInputDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        if (user == null) {
            throw new InvalidTokenUser();
        }

        //이미 MyProfile이 존재하는지 확인
        if(!isMyProfileAlreadySetting(user)){
            throw new InvalidRequestException(ResultCode.MYPROFILE_REQUIRED);
        }
        

        //이미 RoomMateProfile이 존재하는지 확인
        if(isAlreadyExistRommMateProfile(user)){
            throw new InvalidRequestException(ResultCode.ALREADY_EXIST_PROFILE);
        }

        // roomMateProfileInputDTO의 정보를 RoomMateProfile에 변환하여 저장
        RoomMateProfile roomMateProfile = new RoomMateProfile();
        setProfileInformation(roomMateProfileInputDTO, roomMateProfile, user);
        //HopeDormitoriy 리스트를 저장
        saveHopeDormitories(roomMateProfileInputDTO, roomMateProfile);

        //HopeRoomPerson을 저장
        saveHopeRoomPerson(roomMateProfileInputDTO, roomMateProfile);

        //OwnSleepHabit을 저장
        saveOwnSleepHabit(roomMateProfileInputDTO, roomMateProfile);
    }

    private void saveOwnSleepHabit(RoomMateProfileInputDTO roomMateProfileInputDTO, RoomMateProfile roomMateProfile) {
        Set<SleepHabit> sleepHabits = roomMateProfileInputDTO.getSleepHabits();

        //list에 NO_MATTER가 포함되어 있지 않으면 저장
        if (!sleepHabits.contains(SleepHabit.NO_MATTER)) {
            for(SleepHabit sleepHabit : sleepHabits) {
                OwnSleepHabit ownSleepHabitEntity = OwnSleepHabit.builder()
                        .roomMateProfile(roomMateProfile)
                        .sleepHabit(sleepHabit)
                        .build();
                ownSleepHabitRepository.save(ownSleepHabitEntity);
            }
        }
    }

    private void saveHopeRoomPerson(RoomMateProfileInputDTO roomMateProfileInputDTO, RoomMateProfile roomMateProfile) {
        Set<Integer> hopeRoomPersons = roomMateProfileInputDTO.getHopeRoomPersons();
        for (Integer hopeRoomPerson : hopeRoomPersons) {
            HopeRoomPerson hopeRoomPersonEntity = HopeRoomPerson.builder()
                    .roomMateProfile(roomMateProfile)
                    .hopeRoomPerson(hopeRoomPerson)
                    .build();
            hopeRoomPersonRepository.save(hopeRoomPersonEntity);
        }
    }

    private void saveHopeDormitories(RoomMateProfileInputDTO roomMateProfileInputDTO, RoomMateProfile roomMateProfile) {
        //HopeDormitory를 저장
        Set<String> hopeDormitories = roomMateProfileInputDTO.getHopeDormitories();
        for (String hopeDormitory : hopeDormitories) {
            HopeDormitory hopeDormitoryEntity = HopeDormitory.builder()
                    .roomMateProfile(roomMateProfile)
                    .hopeDormitory(hopeDormitory)
                    .build();
            hopeDormitoryRepository.save(hopeDormitoryEntity);
        }
    }

    private void setProfileInformation(RoomMateProfileInputDTO roomMateProfileInputDTO, RoomMateProfile roomMateProfile, User user) {
        roomMateProfile.setCleanPeriod(roomMateProfileInputDTO.getCleanPeriod());
        roomMateProfile.setActivityTime(roomMateProfileInputDTO.getActivityTime());
        roomMateProfile.setColdLevel(roomMateProfileInputDTO.getColdLevel());
        roomMateProfile.setHotLevel(roomMateProfileInputDTO.getHotLevel());
        roomMateProfile.setIsSmoking(roomMateProfileInputDTO.getIsSmoking());
        roomMateProfile.setShowerTime(roomMateProfileInputDTO.getShowerTime());
        roomMateProfile.setMateType(MateType.ROOMMATE);
        roomMateProfile.setUser(user);
        roomMateProfileRepository.save(roomMateProfile);
    }

    private boolean isAlreadyExistRommMateProfile(User user) {
        RoomMateProfile roomMateProfileCheck = roomMateProfileRepository.findByUserId(user.getId()).orElse(null);

        boolean isAlreadyExist = false;
        if (roomMateProfileCheck != null) {
            isAlreadyExist = true;
        }
        return isAlreadyExist;
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
