package com.example.danjamserver.workoutMate.service;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.workoutMate.domain.WorkoutMateProfile;
import com.example.danjamserver.workoutMate.dto.WorkoutMateProfileInputDTO;
import com.example.danjamserver.workoutMate.enums.WorkoutIntensity;
import com.example.danjamserver.workoutMate.enums.WorkoutTime;
import com.example.danjamserver.workoutMate.enums.WorkoutTimeZone;
import com.example.danjamserver.workoutMate.enums.WorkoutType;
import com.example.danjamserver.workoutMate.repository.WorkoutMateProfileRepository;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("WorkoutMateProfile Service 테스트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class WorkoutMateProfileServiceTest {

    @Autowired
    private WorkoutMateProfileService workoutMateProfileService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private CustomUserDetails customUserDetails;

    @Autowired
    private WorkoutMateProfileRepository workoutMateProfileRepository;

    @BeforeEach
    void setUp() {
        when(customUserDetails.getUser()).thenReturn(getUser());
    }

    // 유저 데이터를 반환하는 메소드
    private User getUser() {
        User user = userRepository.findByUsername("testmate2").get();
        return user;
    }

    @Test
    @DisplayName("정상적으로 데이터를 입력할 시, WorkoutMateProfile를 생성한다.")
    @Transactional
    void shouldCreateWorkoutMateProfile() {
        // Given
        WorkoutMateProfileInputDTO workoutMateProfileInputDTO = getWorkoutMateProfileInputDTO();

        // When
        workoutMateProfileService.createWorkoutMateProfile(customUserDetails, workoutMateProfileInputDTO);

        // Then
        // WorkoutMateProfile이 생성되었는지 확인
        WorkoutMateProfile workoutMateProfile = workoutMateProfileRepository.findByUserId(customUserDetails.getUser().getId()).get();
        assertThat(workoutMateProfile).isNotNull();
    }

    private static WorkoutMateProfileInputDTO getWorkoutMateProfileInputDTO() {
        Set<WorkoutTime> workoutTimes = Set.of(WorkoutTime.TWO_THREE, WorkoutTime.THREE_OVER);
        Set<WorkoutType> workoutTypes = Set.of(WorkoutType.AN_AEROBIC, WorkoutType.RACKET_EXERCISE);
        Set<WorkoutTimeZone> workoutTimeZones = Set.of(WorkoutTimeZone.MORNING, WorkoutTimeZone.LUNCH);
        WorkoutIntensity workoutIntensity = WorkoutIntensity.LIGHT_WORKOUT;

        WorkoutMateProfileInputDTO workoutMateProfileInputDTO = new WorkoutMateProfileInputDTO();
        workoutMateProfileInputDTO.setPreferredWorkoutIntensity(workoutIntensity);
        workoutMateProfileInputDTO.setPreferredWorkoutTimes(workoutTimes);
        workoutMateProfileInputDTO.setPreferredWorkoutTypes(workoutTypes);
        workoutMateProfileInputDTO.setPreferredWorkoutTimeZones(workoutTimeZones);
        return workoutMateProfileInputDTO;
    }
}
