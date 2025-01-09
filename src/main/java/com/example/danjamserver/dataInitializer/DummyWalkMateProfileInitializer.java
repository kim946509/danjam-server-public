package com.example.danjamserver.dataInitializer;

import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.walkMate.domain.PreferredWalkIntensity;
import com.example.danjamserver.walkMate.domain.PreferredWalkTimeZone;
import com.example.danjamserver.walkMate.domain.WalkMateProfile;
import com.example.danjamserver.walkMate.enums.WalkIntensity;
import com.example.danjamserver.walkMate.enums.WalkTime;
import com.example.danjamserver.walkMate.enums.WalkTimeZone;
import com.example.danjamserver.walkMate.repository.PreferredWalkIntensityRepository;
import com.example.danjamserver.walkMate.repository.PreferredWalkTimeZoneRepository;
import com.example.danjamserver.walkMate.repository.WalkMateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Order(3)
public class DummyWalkMateProfileInitializer implements ApplicationRunner {

    private final WalkMateProfileRepository walkMateProfileRepository;
    private final UserRepository userRepository;
    private final PreferredWalkIntensityRepository preferredWalkIntensityRepository;
    private final PreferredWalkTimeZoneRepository preferredWalkTimeZoneRepository;
    private final Random random = new Random();

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // 더미 유저가 존재하는지 확인. 존재하지 않으면 더미 메이트 프로필 생성하지 않음
        String checkUsername = "test" + DummyUsersInitializer.startDummyUserNumber;
        User checkUser = userRepository.findByUsername(checkUsername).orElse(null);
        if (checkUser == null) {
            return;
        }

        // 이미 더미 유저의 산책 메이트 프로필이 존재하는지 확인. 존재하면 더미 산책 메이트 프로필 생성하지 않음
        WalkMateProfile checkWalkMateProfile = walkMateProfileRepository.findByUserId(checkUser.getId()).orElse(null);
        if (checkWalkMateProfile != null) {
            return;
        }

        // 더미 산책 메이트 프로필 생성
        String dummyUsername = "test";
        // 더미 유저들의 산책 메이트 프로필 생성
        for (int i = DummyUsersInitializer.startDummyUserNumber; i <= DummyUsersInitializer.endDummyUserNumber; i++) {
            User user = userRepository.findByUsername(dummyUsername + i).orElse(null);
            if (user == null) {
                continue;
            }

            // 이미 WalkMateProfile이 존재하는지 확인
            WalkMateProfile walkMateProfileCheck = walkMateProfileRepository.findByUserId(user.getId()).orElse(null);
            if (walkMateProfileCheck != null) {
                continue;
            }

            //WalkMateProfile 저장
            WalkMateProfile walkMateProfile = setRandomWalkMateProfile(user);

            //PreferredWalkTimeZone 저장
            setRandomWalkTimeZone(walkMateProfile);

            //PreferredWalkIntensity 저장
            setRandomWalkIntensity(walkMateProfile);

        }

    }



    private WalkMateProfile setRandomWalkMateProfile(User user) {
        WalkMateProfile walkMateProfile = WalkMateProfile.builder()
                .user(user)
                .mateType(MateType.WALKMATE)
                .preferredWalkTime(getRandomWalkTime())
                .build();
        walkMateProfileRepository.save(walkMateProfile);
        return walkMateProfile;
    }

    private WalkTime getRandomWalkTime() {
        WalkTime[] walkTimes = WalkTime.values();
        int randomIndex = random.nextInt(walkTimes.length);
        return walkTimes[randomIndex];
    }

    private List<WalkTimeZone> getRandomWalkTimeZones() {
        List<WalkTimeZone> walkTimeZones = new ArrayList<>(Arrays.stream(WalkTimeZone.values()).toList());
        //랜덤으로 1~4개의 산책 시간대를 선택. 중복 선택 불가
        int randomSize = random.nextInt(4) + 1;
        List<WalkTimeZone> walkTimeZonesList = new ArrayList<>();
        for (int i = 0; i < randomSize; i++) {
            int randomIndex = random.nextInt(walkTimeZones.size());
            walkTimeZonesList.add(walkTimeZones.get(randomIndex));
            walkTimeZones.removeIf(walkTimeZonesList::contains);
        }

        return walkTimeZonesList;
    }

    private void setRandomWalkTimeZone(WalkMateProfile walkMateProfile) {
        List<WalkTimeZone> walkTimeZones = getRandomWalkTimeZones();
        walkTimeZones.forEach(walkTimeZone -> {
            PreferredWalkTimeZone preferredWalkTimeZone = PreferredWalkTimeZone.builder()
                    .walkMateProfile(walkMateProfile)
                    .walkTimeZone(walkTimeZone)
                    .build();
            preferredWalkTimeZoneRepository.save(preferredWalkTimeZone);
        });
    }


    private List<WalkIntensity> getRandomWalkIntensities() {
        List<WalkIntensity> walkIntensities = new ArrayList<>(Arrays.stream(WalkIntensity.values()).toList());
        //랜덤으로 1~3개의 산책 강도를 선택. 중복 선택 불가
        int randomSize = random.nextInt(3) + 1;
        List<WalkIntensity> walkIntensitiesList = new ArrayList<>();
        for (int i = 0; i < randomSize; i++) {
            int randomIndex = random.nextInt(walkIntensities.size());
            walkIntensitiesList.add(walkIntensities.get(randomIndex));
            walkIntensities.removeIf(walkIntensitiesList::contains);
        }
        return walkIntensitiesList;
    }

    private void setRandomWalkIntensity(WalkMateProfile walkMateProfile) {
        List<WalkIntensity> walkIntensities = getRandomWalkIntensities();
        walkIntensities.forEach(walkIntensity -> {
            PreferredWalkIntensity preferredWalkIntensity = PreferredWalkIntensity.builder()
                    .walkMateProfile(walkMateProfile)
                    .walkIntensity(walkIntensity)
                    .build();
            preferredWalkIntensityRepository.save(preferredWalkIntensity);
        });
    }
}
