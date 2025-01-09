package com.example.danjamserver.mate.service;

import com.example.danjamserver.foodMate.domain.FoodMateProfile;
import com.example.danjamserver.foodMate.domain.HopeMenu;
import com.example.danjamserver.foodMate.domain.MealMateTime;
import com.example.danjamserver.foodMate.dto.FoodMateProfileInputDTO;
import com.example.danjamserver.foodMate.enums.MateTime;
import com.example.danjamserver.foodMate.enums.Menu;
import com.example.danjamserver.foodMate.repository.FoodMateProfileRepository;
import com.example.danjamserver.foodMate.repository.HopeMenuRepository;
import com.example.danjamserver.foodMate.repository.MealMateTimeRepository;
import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.roomMate.domain.HopeDormitory;
import com.example.danjamserver.roomMate.domain.HopeRoomPerson;
import com.example.danjamserver.roomMate.domain.OwnSleepHabit;
import com.example.danjamserver.roomMate.domain.RoomMateProfile;
import com.example.danjamserver.roomMate.dto.RoomMateProfileInputDTO;
import com.example.danjamserver.roomMate.enums.SleepHabit;
import com.example.danjamserver.roomMate.repository.HopeDormitoryRepository;
import com.example.danjamserver.roomMate.repository.HopeRoomPersonRepository;
import com.example.danjamserver.roomMate.repository.OwnSleepHabitRepository;
import com.example.danjamserver.roomMate.repository.RoomMateProfileRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.studyMate.domain.PreferredStudyType;
import com.example.danjamserver.studyMate.domain.StudyMateProfile;
import com.example.danjamserver.studyMate.domain.UserSubject;
import com.example.danjamserver.studyMate.dto.StudyMateProfileInputDTO;
import com.example.danjamserver.studyMate.enums.StudyType;
import com.example.danjamserver.studyMate.repository.PreferredStudyTypeRepository;
import com.example.danjamserver.studyMate.repository.StudyMateProfileRepository;
import com.example.danjamserver.studyMate.repository.UserSubjectRepository;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.util.exception.InvalidRequestException;
import com.example.danjamserver.util.exception.InvalidTokenUser;
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
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTime;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTimeZone;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutType;
import com.example.danjamserver.workoutMate.domain.WorkoutMateProfile;
import com.example.danjamserver.workoutMate.dto.WorkoutMateProfileInputDTO;
import com.example.danjamserver.workoutMate.enums.WorkoutIntensity;
import com.example.danjamserver.workoutMate.enums.WorkoutTime;
import com.example.danjamserver.workoutMate.enums.WorkoutTimeZone;
import com.example.danjamserver.workoutMate.enums.WorkoutType;
import com.example.danjamserver.workoutMate.repository.PreferredWorkoutTimeRepository;
import com.example.danjamserver.workoutMate.repository.PreferredWorkoutTimeZoneRepository;
import com.example.danjamserver.workoutMate.repository.PreferredWorkoutTypeRepository;
import com.example.danjamserver.workoutMate.repository.WorkoutMateProfileRepository;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MateProfileModifyService {
    private final RoomMateProfileRepository roomMateProfileRepository;
    private final HopeDormitoryRepository hopeDormitoryRepository;
    private final HopeRoomPersonRepository hopeRoomPersonRepository;
    private final OwnSleepHabitRepository ownSleepHabitRepository;
    private final FoodMateProfileRepository foodMateProfileRepository;
    private final HopeMenuRepository hopeMenuRepository;
    private final MealMateTimeRepository mealMateTimeRepository;
    private final WalkMateProfileRepository walkMateProfileRepository;
    private final PreferredWalkIntensityRepository preferredWalkIntensityRepository;
    private final PreferredWalkTimeZoneRepository preferredWalkTimeZoneRepository;
    private final PreferredWorkoutTimeRepository preferredWorkoutTimeRepository;
    private final PreferredWorkoutTypeRepository preferredWorkoutTypeRepository;
    private final PreferredWorkoutTimeZoneRepository preferredWorkoutTimeZoneRepository;
    private final WorkoutMateProfileRepository workoutMateProfileRepository;
    private final StudyMateProfileRepository studyMateProfileRepository;
    private final PreferredStudyTypeRepository preferredStudyTypeRepository;
    private final UserSubjectRepository userSubjectRepository;


    /**
     * 룸 메이트 수정
     *
     * @param roomMateProfileInputDTO
     * @param customUserDetails
     */
    @Transactional
    public void updateRoomMateProfile(RoomMateProfileInputDTO roomMateProfileInputDTO,
                                      CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        RoomMateProfile roomMateProfile = roomMateProfileRepository.findByUserId(user.getId()).orElse(null);

        if (user == null) {
            throw new InvalidTokenUser();
        }

        //roomMate 가 이미 존재하지 않으면 에러처리
        if (roomMateProfile == null) {
            throw new InvalidRequestException(ResultCode.CAN_NOT_FIND_USER_PROFILE);
        }

        // 업데이트할 필드 설정
        roomMateProfile.setCleanPeriod(roomMateProfileInputDTO.getCleanPeriod());
        roomMateProfile.setActivityTime(roomMateProfileInputDTO.getActivityTime());
        roomMateProfile.setColdLevel(roomMateProfileInputDTO.getColdLevel());
        roomMateProfile.setHotLevel(roomMateProfileInputDTO.getHotLevel());
        roomMateProfile.setIsSmoking(roomMateProfileInputDTO.getIsSmoking());
        roomMateProfile.setShowerTime(roomMateProfileInputDTO.getShowerTime());
        roomMateProfileRepository.save(roomMateProfile);

        //기존 데이터 삭제
        hopeRoomPersonRepository.deleteAllByRoomMateProfileId(roomMateProfile.getId());
        hopeDormitoryRepository.deleteAllByRoomMateProfileId(roomMateProfile.getId());
        ownSleepHabitRepository.deleteAllByRoomMateProfileId(roomMateProfile.getId());

        // HopeDormitory 업데이트
        Set<String> hopeDormitories = roomMateProfileInputDTO.getHopeDormitories();
        for (String hopeDormitory : hopeDormitories) {
            HopeDormitory hopeDormitoryEntity = HopeDormitory.builder()
                    .roomMateProfile(roomMateProfile)
                    .hopeDormitory(hopeDormitory)
                    .build();
            hopeDormitoryRepository.save(hopeDormitoryEntity);
        }

        // HopeRoomPerson 업데이트
        Set<Integer> hopeRoomPersons = roomMateProfileInputDTO.getHopeRoomPersons();
        for (Integer hopeRoomPerson : hopeRoomPersons) {
            HopeRoomPerson hopeRoomPersonEntity = HopeRoomPerson.builder()
                    .roomMateProfile(roomMateProfile)
                    .hopeRoomPerson(hopeRoomPerson)
                    .build();
            hopeRoomPersonRepository.save(hopeRoomPersonEntity);
        }

        // OwnSleepHabit 업데이트
        Set<SleepHabit> sleepHabits = roomMateProfileInputDTO.getSleepHabits();
        for (SleepHabit sleepHabit : sleepHabits) {
            OwnSleepHabit ownSleepHabitEntity = OwnSleepHabit.builder()
                    .roomMateProfile(roomMateProfile)
                    .sleepHabit(sleepHabit)
                    .build();
            ownSleepHabitRepository.save(ownSleepHabitEntity);
        }
    }


    /**
     * 식사 메이트 수정
     *
     * @param foodMateProfileInputDto
     * @param customUserDetails
     */
    @Transactional
    public void updateFoodMateProfile(FoodMateProfileInputDTO foodMateProfileInputDto,
                                      CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        if (user == null) {
            throw new InvalidTokenUser();
        }

        FoodMateProfile foodMateProfile = foodMateProfileRepository.findByUserId(user.getId()).orElse(null);

        //foodMate정 가 이미 존재하지 않으면 에러처리
        if (foodMateProfile == null) {
            throw new InvalidRequestException(ResultCode.CAN_NOT_FIND_USER_PROFILE);
        }

        //기존 프로필 업데이트
        foodMateProfile.setMealTime(foodMateProfileInputDto.getMealTime());
        foodMateProfile.setDiningManner(foodMateProfileInputDto.getDiningManner());
        foodMateProfile.setAllergiesMap(foodMateProfileInputDto.getAllergies());

        mealMateTimeRepository.deleteAllByFoodMateProfileId(foodMateProfile.getId());
        hopeMenuRepository.deleteAllByFoodMateProfileId(foodMateProfile.getId());

        //식사메이트 필요시간 저장
        Set<MateTime> mateTime = foodMateProfileInputDto.getMateTime();

        for (MateTime mateTimes : mateTime) {
            MealMateTime mealMateTimeEntity = MealMateTime.builder()
                    .foodMateProfile(foodMateProfile)
                    .mateTime(mateTimes)
                    .build();
            mealMateTimeRepository.save(mealMateTimeEntity);
        }

        //선호 음식 저장
        Set<Menu> menuList = foodMateProfileInputDto.getMenu();

        for (Menu menus : menuList) {
            HopeMenu menuEntity = HopeMenu.builder()
                    .foodMateProfile(foodMateProfile)
                    .menus(menus)
                    .build();
            hopeMenuRepository.save(menuEntity);
        }

        foodMateProfileRepository.save(foodMateProfile);
    }


    /**
     * 산책 메이트 프로필 수정
     *
     * @param customUserDetails
     * @param walkMateProfileInputDTO
     */
    @Transactional
    public void updateWalkMateProfile(WalkMateProfileInputDTO walkMateProfileInputDTO,
                                      CustomUserDetails customUserDetails) {

        User user = customUserDetails.getUser();

        if (user == null) {
            throw new InvalidTokenUser();
        }

        WalkMateProfile walkMateProfile = walkMateProfileRepository.findByUserId(user.getId()).orElse(null);

        //walkmate 가 존재하지 않으면 에러처리
        if (walkMateProfile == null) {
            throw new InvalidRequestException(ResultCode.CAN_NOT_FIND_USER_PROFILE);
        }

        // 선호하는 걷기 시간 저장
        walkMateProfile.setPreferredWalkTime(walkMateProfileInputDTO.getPreferredWalkTime());

        // 유저 정보 저장
        walkMateProfile.setUser(user);

        // MateType 저장
        walkMateProfile.setMateType(MateType.WALKMATE);

        preferredWalkTimeZoneRepository.deleteAllByWalkMateProfileId(walkMateProfile.getId());
        preferredWalkIntensityRepository.deleteAllByWalkMateProfileId(walkMateProfile.getId());

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

        walkMateProfileRepository.save(walkMateProfile);
    }

    /**
     * 운동 메이트 프로필 수정
     *
     * @param workoutMateProfileInputDTO
     * @param customUserDetails
     */
    @Transactional
    public void updateWorkoutMateProfile(WorkoutMateProfileInputDTO workoutMateProfileInputDTO,
                                         CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        if (user == null) {
            throw new InvalidTokenUser();
        }

        WorkoutMateProfile workoutMateProfile = workoutMateProfileRepository.findByUserId(user.getId())
                .orElse(null);

        //walkmate 가 존재하지 않으면 에러처리
        if (workoutMateProfile == null) {
            throw new InvalidRequestException(ResultCode.CAN_NOT_FIND_USER_PROFILE);
        }

        // 선호하는 운동 강도 저장
        WorkoutIntensity workoutIntensity = workoutMateProfileInputDTO.getPreferredWorkoutIntensity();
        workoutMateProfile.setPreferredWorkoutIntensity(workoutIntensity);
        workoutMateProfile.setUser(user);
        workoutMateProfile.setMateType(MateType.WORKOUTMATE);

        preferredWorkoutTimeRepository.deleteAllByWorkoutMateProfileId(workoutMateProfile.getId());
        preferredWorkoutTypeRepository.deleteAllByWorkoutMateProfileId(workoutMateProfile.getId());
        preferredWorkoutTimeZoneRepository.deleteAllByWorkoutMateProfileId(workoutMateProfile.getId());

        // preferredWorkoutTime 저장
        Set<WorkoutTime> preferredWorkoutTimes = workoutMateProfileInputDTO.getPreferredWorkoutTimes();
        for (WorkoutTime preferredWorkoutTime : preferredWorkoutTimes) {
            PreferredWorkoutTime preferredWorkoutTimeEntity = PreferredWorkoutTime.builder()
                    .workoutMateProfile(workoutMateProfile)
                    .workoutTime(preferredWorkoutTime)
                    .build();
            preferredWorkoutTimeRepository.save(preferredWorkoutTimeEntity);
        }

        // preferredWorkoutType 저장
        Set<WorkoutType> preferredWorkoutTypes = workoutMateProfileInputDTO.getPreferredWorkoutTypes();
        for (WorkoutType preferredWorkoutType : preferredWorkoutTypes) {
            PreferredWorkoutType preferredWorkoutTypeEntity = PreferredWorkoutType.builder()
                    .workoutMateProfile(workoutMateProfile)
                    .workoutType(preferredWorkoutType)
                    .build();
            preferredWorkoutTypeRepository.save(preferredWorkoutTypeEntity);
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

        workoutMateProfileRepository.save(workoutMateProfile);
    }

    /**
     * 스터디 메이트 수정
     */
    @Transactional
    public void updateStudyMateProfile(StudyMateProfileInputDTO studyMateProfileInputDTO,
                                       CustomUserDetails customUserDetails) {

        User user = customUserDetails.getUser();

        if (user == null) {
            throw new InvalidTokenUser();
        }

        StudyMateProfile studyMateProfile = studyMateProfileRepository.findByUserId(user.getId())
                .orElse(null);

        //studyMate 가 존재하지 않으면 에러처리
        if (studyMateProfile == null) {
            throw new InvalidRequestException(ResultCode.CAN_NOT_FIND_USER_PROFILE);
        }

        //선호하는 스터디 소요 시간 저장
        studyMateProfile.setPreferredStudyTime(studyMateProfileInputDTO.getPreferredStudyTime());
        //성적 정보 저장
        studyMateProfile.setAverageGrade(studyMateProfileInputDTO.getAverageGrade());
        //유저 정보 저장
        studyMateProfile.setUser(user);
        //MateType 저장
        studyMateProfile.setMateType(MateType.STUDYMATE);
        studyMateProfileRepository.save(studyMateProfile);

        preferredStudyTypeRepository.deleteAllByStudyMateProfileId(studyMateProfile.getId());
        userSubjectRepository.deleteAllByStudyMateProfileId(studyMateProfile.getId());

        //일대다 관계(중복선택) 항목들 저장
        //선호하는 스터디 타입 저장
        Set<StudyType> preferredStudyTypes = studyMateProfileInputDTO.getPreferredStudyTypes();
        for (StudyType preferredStudyType : preferredStudyTypes) {
            preferredStudyTypeRepository.save(PreferredStudyType.builder()
                    .studyMateProfile(studyMateProfile)
                    .studyType(preferredStudyType)
                    .build());
        }

        //수강중인 과목 저장
        Set<String> preferredStudySubjects = studyMateProfileInputDTO.getSubjects();
        if(preferredStudySubjects.size() !=0) {
            Set<String> userSubjects = studyMateProfileInputDTO.getSubjects();
            for (String userSubject : userSubjects) {
                userSubjectRepository.save(UserSubject.builder()
                        .studyMateProfile(studyMateProfile)
                        .subject(userSubject)
                        .build());
            }
        }
    }

}
