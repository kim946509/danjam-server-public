package com.example.danjamserver.dataInitializer;

import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.roomMate.domain.HopeDormitory;
import com.example.danjamserver.roomMate.domain.HopeRoomPerson;
import com.example.danjamserver.roomMate.domain.OwnSleepHabit;
import com.example.danjamserver.roomMate.domain.RoomMateProfile;
import com.example.danjamserver.roomMate.enums.ActivityTime;
import com.example.danjamserver.roomMate.enums.CleanPeriod;
import com.example.danjamserver.roomMate.enums.Level;
import com.example.danjamserver.roomMate.enums.ShowerTime;
import com.example.danjamserver.roomMate.enums.SleepHabit;
import com.example.danjamserver.roomMate.repository.HopeDormitoryRepository;
import com.example.danjamserver.roomMate.repository.HopeRoomPersonRepository;
import com.example.danjamserver.roomMate.repository.OwnSleepHabitRepository;
import com.example.danjamserver.roomMate.repository.RoomMateProfileRepository;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(3)
public class DummyRoomMateProfileInitializer implements ApplicationRunner {

    private final RoomMateProfileRepository roomMateProfileRepository;
    private final UserRepository userRepository;
    private final HopeDormitoryRepository hopeDormitoryRepository;
    private final HopeRoomPersonRepository hopeRoomPersonRepository;
    private final OwnSleepHabitRepository ownSleepHabitRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // 더미 유저가 존재하는지 확인. 존재하지 않으면 메이트 프로필 생성하지 않음
        String checkUsername = "test" + DummyUsersInitializer.startDummyUserNumber;
        User checkUser = userRepository.findByUsername(checkUsername).orElse(null);
        if (checkUser == null) {
            return;
        }

        // 이미 더미 유저의 룸메이트 프로필이 존재하는지 확인. 존재하면 더미 룸메이트 프로필 생성하지 않음
        RoomMateProfile checkRoomMateProfile = roomMateProfileRepository.findByUserId(checkUser.getId()).orElse(null);
        if(checkRoomMateProfile != null) {
            return;
        }

        // 더미 룸메이트 프로필 생성
        String dummyUsername = "test";
        // 더미 유저들의 룸메이트 프로필 생성
        for (int i = DummyUsersInitializer.startDummyUserNumber; i <= DummyUsersInitializer.endDummyUserNumber; i++) {
            User user = userRepository.findByUsername(dummyUsername + i).orElse(null);
            if(user == null) {
                continue;
            }

            //흡연 유무 랜덤
            Boolean isSmoking = Math.random() < 0.5;

            // 더위 타는 정도 랜덤
            Level hotLevel = getRandomLevel();

            // 추위 타는 정도 랜덤
            Level coldLevel = getRandomLevel();

            // 활동 시간 랜덤
            ActivityTime activityTime = getRandomActivityTime();
            
            // 청소 주기 랜덤
            CleanPeriod cleanPeriod = getRandomCleanPeriod();
            
            // 샤워 시간 랜덤
            ShowerTime showerTime = getRandomShowerTime();
            
            // 룸메이트 프로필 생성
            RoomMateProfile roomMateProfile = getRoomMateProfile(isSmoking, hotLevel, coldLevel, activityTime, cleanPeriod, showerTime, user);
            roomMateProfile.setMateType(MateType.ROOMMATE);
            roomMateProfileRepository.save(roomMateProfile);

            //HopeDormitory를 저장
            List<String> hopeDormitories = getHopeDormitories(user.getSchool());
            for (String hopeDormitory : hopeDormitories) {
                HopeDormitory hopeDormitoryEntity = HopeDormitory.builder()
                        .roomMateProfile(roomMateProfile)
                        .hopeDormitory(hopeDormitory)
                        .build();
                hopeDormitoryRepository.save(hopeDormitoryEntity);
            }

            //HopeRoomPerson을 저장
            List<Integer> hopeRoomPersons = getRandomHopeRoomPerson();
            for (Integer hopeRoomPerson : hopeRoomPersons) {
                HopeRoomPerson hopeRoomPersonEntity = HopeRoomPerson.builder()
                        .roomMateProfile(roomMateProfile)
                        .hopeRoomPerson(hopeRoomPerson)
                        .build();
                hopeRoomPersonRepository.save(hopeRoomPersonEntity);
            }

            //Own Sleep Habit을 저장
            List<SleepHabit> ownSleepHabits = getRandomOwnSleepHabit();
            for (SleepHabit ownSleepHabit : ownSleepHabits) {
                OwnSleepHabit ownSleepHabitEntity = OwnSleepHabit.builder()
                        .roomMateProfile(roomMateProfile)
                        .sleepHabit(ownSleepHabit)
                        .build();
                ownSleepHabitRepository.save(ownSleepHabitEntity);
            }
        }
    }

    private List<SleepHabit> getRandomOwnSleepHabit(){
        List<SleepHabit> ownSleepHabits = new ArrayList<>();
        ownSleepHabits.add(SleepHabit.SENSITIVE_TO_LIGHT);
        ownSleepHabits.add(SleepHabit.NO_MATTER);
        ownSleepHabits.add(SleepHabit.SENSITIVE_TO_SOUND);
        ownSleepHabits.add(SleepHabit.GRIND_TEETH);
        ownSleepHabits.add(SleepHabit.SNORE);

        //해당 수면 습관들중에 랜덤으로 1~4개 선택
        int random = (int) (Math.random() * 4) + 1;
        List<SleepHabit> selectedOwnSleepHabits = new ArrayList<>();
        for(int i = 0; i < random; i++){
            int index = (int) (Math.random() * ownSleepHabits.size());
            selectedOwnSleepHabits.add(ownSleepHabits.get(index));
            if(ownSleepHabits.get(index) == SleepHabit.NO_MATTER){
                List<SleepHabit> nothing = new ArrayList<>();
                nothing.add(SleepHabit.NO_MATTER);
                return nothing;
            }
            ownSleepHabits.remove(index);
        }
        return selectedOwnSleepHabits;
    }

    private RoomMateProfile getRoomMateProfile(Boolean isSmoking, Level hotLevel, Level coldLevel, ActivityTime activityTime, CleanPeriod cleanPeriod, ShowerTime showerTime, User user) {
        return RoomMateProfile.builder()
                .isSmoking(isSmoking)
                .hotLevel(hotLevel)
                .coldLevel(coldLevel)
                .activityTime(activityTime)
                .cleanPeriod(cleanPeriod)
                .showerTime(showerTime)
                .user(user)
                .build();
    }

    private List<Integer> getRandomHopeRoomPerson(){
        List<Integer> hopeRoomPersons = new ArrayList<>();
        hopeRoomPersons.add(2);
        hopeRoomPersons.add(3);
        hopeRoomPersons.add(4);

        //해당 인원들중에 랜덤으로 1~3개 선택
        int random = (int) (Math.random() * 3) + 1;
        List<Integer> selectedHopeRoomPersons = new ArrayList<>();
        for(int i = 0; i < random; i++){
            int index = (int) (Math.random() * hopeRoomPersons.size());
            selectedHopeRoomPersons.add(hopeRoomPersons.get(index));
            hopeRoomPersons.remove(index);
        }
        return selectedHopeRoomPersons;
    }

    private Level getRandomLevel(){
        return Level.values()[(int) (Math.random() * Level.values().length)];
    }

    private ActivityTime getRandomActivityTime(){
        return ActivityTime.values()[(int) (Math.random() * ActivityTime.values().length)];
    }

    private CleanPeriod getRandomCleanPeriod(){
        return CleanPeriod.values()[(int) (Math.random() * CleanPeriod.values().length)];
    }

    private ShowerTime getRandomShowerTime(){
        return ShowerTime.values()[(int) (Math.random() * ShowerTime.values().length)];
    }

    private List<String> getHopeDormitories(School school){
        //고려대학교 세종캠퍼스
        if(school.getId() == 1){
            List<String> dormitories = new ArrayList<>();
            dormitories.add("자유관");
            dormitories.add("진리관");
            dormitories.add("정의관");
            dormitories.add("미래관");

            //해당 기숙사들중에 랜덤으로 1~4개 선택
            int random = (int) (Math.random() * 4) + 1;
            List<String> selectedDormitories = new ArrayList<>();
            for(int i = 0; i < random; i++){
                int index = (int) (Math.random() * dormitories.size());
                selectedDormitories.add(dormitories.get(index));
                dormitories.remove(index);
            }

            return selectedDormitories;
        }

        // 홍익대학교 세종캠퍼스
        if(school.getId() == 2){
            List<String> dormitories = new ArrayList<>();
            dormitories.add("두루암");
            dormitories.add("새로암");

            //해당 기숙사들중에 랜덤으로 1~2개 선택
            int random = (int) (Math.random() * 2) + 1;
            List<String> selectedDormitories = new ArrayList<>();
            for(int i = 0; i < random; i++){
                int index = (int) (Math.random() * dormitories.size());
                selectedDormitories.add(dormitories.get(index));
                dormitories.remove(index);
            }

            return selectedDormitories;
        }
        
        else 
            return null;
    }
}
