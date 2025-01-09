package com.example.danjamserver.dataInitializer;

import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTime;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTimeZone;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutType;
import com.example.danjamserver.workoutMate.domain.WorkoutMateProfile;
import com.example.danjamserver.workoutMate.enums.WorkoutIntensity;
import com.example.danjamserver.workoutMate.enums.WorkoutTime;
import com.example.danjamserver.workoutMate.enums.WorkoutTimeZone;
import com.example.danjamserver.workoutMate.enums.WorkoutType;
import com.example.danjamserver.workoutMate.repository.PreferredWorkoutTimeRepository;
import com.example.danjamserver.workoutMate.repository.PreferredWorkoutTimeZoneRepository;
import com.example.danjamserver.workoutMate.repository.PreferredWorkoutTypeRepository;
import com.example.danjamserver.workoutMate.repository.WorkoutMateProfileRepository;
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
public class DummyWorkoutMateProfileInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final WorkoutMateProfileRepository workoutMateProfileRepository;
    private final PreferredWorkoutTimeRepository preferredWorkoutTimeRepository;
    private final PreferredWorkoutTimeZoneRepository preferredWorkoutTimeZoneRepository;
    private final PreferredWorkoutTypeRepository preferredWorkoutTypeRepository;
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

        // 이미 더미 유저의 운동 메이트 프로필이 존재하는지 확인. 존재하면 더미 운동 메이트 프로필 생성하지 않음
        WorkoutMateProfile checkWorkoutMateProfile = workoutMateProfileRepository.findByUserId(checkUser.getId()).orElse(null);
        if(checkWorkoutMateProfile != null) {
            return;
        }

        // 더미 운동 메이트 프로필 생성
        String dummyUsername = "test";
        for(int i = DummyUsersInitializer.startDummyUserNumber; i<=DummyUsersInitializer.endDummyUserNumber; i++) {
            User user = userRepository.findByUsername(dummyUsername + i).orElse(null);
            if(user == null) {
                continue;
            }

            // 이미 해당 더미유저의 WorkoutMateProfile이 존재하는지 확인
            WorkoutMateProfile checkUserWorkoutMateProfile = workoutMateProfileRepository.findByUserId(user.getId()).orElse(null);
            if(checkUserWorkoutMateProfile != null) {
                continue;
            }

            // WorkoutMateProfile 생성 및 저장
            WorkoutMateProfile workoutMateProfile = setRandomWorkoutMateProfile(user);

            // PreferredWorkoutTime 저장
            setRandomWorkoutTime(workoutMateProfile);

            //PreferredWorkoutTimeZone 저장
            setRandomWorkoutTimeZone(workoutMateProfile);

            // PreferredWorkoutType 저장
            setRandomWorkoutType(workoutMateProfile);

        }

    }

    // 더미 운동 메이트 프로필 생성
    private WorkoutMateProfile setRandomWorkoutMateProfile(User user){
        WorkoutMateProfile randomWorkoutMateProfile = WorkoutMateProfile.builder()
                .user(user)
                .mateType(MateType.WORKOUTMATE)
                .preferredWorkoutIntensity(getRandomWorkoutIntensity())
                .build();
        workoutMateProfileRepository.save(randomWorkoutMateProfile);
        return randomWorkoutMateProfile;
    }

    // 랜덤 WorkoutIntensity 생성
    private WorkoutIntensity getRandomWorkoutIntensity(){
        WorkoutIntensity[] workoutIntensities = WorkoutIntensity.values();
        int randomIndex = random.nextInt(workoutIntensities.length);
        return workoutIntensities[randomIndex];
    }

    // List<WorkoutTime< 저장
    private void setRandomWorkoutTime(WorkoutMateProfile workoutMateProfile){
        List<WorkoutTime> randomWorkoutTimes = getRandomWorkoutTime();
        for(WorkoutTime workoutTime : randomWorkoutTimes) {
            preferredWorkoutTimeRepository.save(preferredWorkoutTimeRepository.save(PreferredWorkoutTime.builder()
                    .workoutMateProfile(workoutMateProfile)
                    .workoutTime(workoutTime)
                    .build()));
        }
    }

    // 랜덤 WorkoutTime 생성
    private List<WorkoutTime> getRandomWorkoutTime(){
        List<WorkoutTime> workoutTimes = new ArrayList<>(Arrays.stream(WorkoutTime.values()).toList());
        // WorkoutTime을 랜덤으로 1개 ~ 4개 선택
        int randomCount = random.nextInt(4) + 1;
        List<WorkoutTime> randomWorkoutTimes = new ArrayList<>();
        for(int i=0; i<randomCount; i++) {
            int randomIndex = random.nextInt(workoutTimes.size());
            randomWorkoutTimes.add(workoutTimes.get(randomIndex));
            workoutTimes.removeIf(randomWorkoutTimes::contains);
        }
        return randomWorkoutTimes;
    }

    // List<WorkoutTimeZone> 저장
    private void setRandomWorkoutTimeZone(WorkoutMateProfile workoutMateProfile){
        List<WorkoutTimeZone> randomWorkoutTimeZones = getRandomWorkoutTimeZones();
        for(WorkoutTimeZone workoutTimeZone : randomWorkoutTimeZones) {
            preferredWorkoutTimeZoneRepository.save(preferredWorkoutTimeZoneRepository.save(PreferredWorkoutTimeZone.builder()
                    .workoutMateProfile(workoutMateProfile)
                    .workoutTimeZone(workoutTimeZone)
                    .build()));
        }
    }

    //랜덤 WworkoutTimeZone 생성
    private List<WorkoutTimeZone> getRandomWorkoutTimeZones(){
        List<WorkoutTimeZone> workoutTimeZones = new ArrayList<>(Arrays.stream(WorkoutTimeZone.values()).toList());
        // WorkoutTimeZone을 랜덤으로 1개 ~ 4개 선택
        int randomCount = random.nextInt(4) + 1;
        List<WorkoutTimeZone> randomWorkoutTimeZones = new ArrayList<>();
        for(int i=0; i<randomCount; i++) {
            int randomIndex = random.nextInt(workoutTimeZones.size());
            randomWorkoutTimeZones.add(workoutTimeZones.get(randomIndex));
            workoutTimeZones.removeIf(randomWorkoutTimeZones::contains);
        }
        return randomWorkoutTimeZones;
    }

    // WorkoutType List 저장
    private void setRandomWorkoutType(WorkoutMateProfile workoutMateProfile){
        List<WorkoutType> randomWorkoutTypes = getRandomWorkoutType();
        for(WorkoutType workoutType : randomWorkoutTypes) {
            preferredWorkoutTypeRepository.save(preferredWorkoutTypeRepository.save(PreferredWorkoutType.builder()
                    .workoutMateProfile(workoutMateProfile)
                    .workoutType(workoutType)
                    .build()));
        }
    }

    // 랜덤 WorkoutType 생성
    private List<WorkoutType> getRandomWorkoutType(){
        List<WorkoutType> workoutTypes = new ArrayList<>(Arrays.stream(WorkoutType.values()).toList());

        // WorkoutType을 랜덤으로 1개 ~ 6개 선택. 중복 선택 불가
        int randomCount = random.nextInt(6) + 1;
        List<WorkoutType> randomWorkoutTypes = new ArrayList<>();
        for(int i=0; i<randomCount; i++) {
            int randomIndex = random.nextInt(workoutTypes.size());
            WorkoutType workoutType = workoutTypes.get(randomIndex);
            if(workoutType == WorkoutType.ANYWAY) {
                return new ArrayList<>(List.of(WorkoutType.ANYWAY));
            }
            randomWorkoutTypes.add(workoutTypes.get(randomIndex));
            workoutTypes.removeIf(randomWorkoutTypes::contains);
        }
        return randomWorkoutTypes;
    }
}
