package com.example.danjamserver.roomMate.service;

import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.roomMate.domain.RoomMateProfile;
import com.example.danjamserver.roomMate.dto.RoomMateProfileInputDTO;
import com.example.danjamserver.roomMate.enums.ActivityTime;
import com.example.danjamserver.roomMate.enums.CleanPeriod;
import com.example.danjamserver.roomMate.enums.Level;
import com.example.danjamserver.roomMate.enums.ShowerTime;
import com.example.danjamserver.roomMate.enums.SleepHabit;
import com.example.danjamserver.roomMate.repository.HopeDormitoryRepository;
import com.example.danjamserver.roomMate.repository.HopeRoomPersonRepository;
import com.example.danjamserver.roomMate.repository.OwnSleepHabitRepository;
import com.example.danjamserver.roomMate.repository.RoomMateProfileRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomMateProfileService 테스트")
class RoomMateProfileServiceTest {

    @Mock
    private RoomMateProfileRepository roomMateProfileRepository;

    @Mock
    private HopeDormitoryRepository hopeDormitoryRepository;

    @Mock
    private HopeRoomPersonRepository hopeRoomPersonRepository;

    @Mock
    private OwnSleepHabitRepository ownSleepHabitRepository;

    @InjectMocks
    private RoomMateProfileService roomMateProfileService;

    @Mock
    private CustomUserDetails customUserDetails;
//    private User user;

//    @BeforeEach
//    void setUp() {
//        user = new User();
//        user.setUsername("testUser");
//        user.setNickname("testNick");
//        user.setPassword("testPassword");
//        user.setEmail("testEmail");
//        user.setGender(1);
//        user.setSchool(new School());
//
//        customUserDetails = new CustomUserDetails(user);  // CustomUserDetails 객체 생성
//    }

    // 정상적으로 RoomMateProfile을 생성하는지 확인하는 테스트.
    /**
     * 'roomMateProfile을 생성한다.'
     * 테스트 시나리오.
     * 1. RoomMateProfileDTO를 생성하고, RoomMateProfile을 생성한다.
     * 2. RoomMateProfileDTO에 담긴 정보를 RoomMateProfile에 저장한다.
     * 3. RoomMateProfile에 저장된 정보를 DB에 저장한다.
     * 4. RoomMateProfile에 저장된 정보를 DB에 저장한다.
     */
    @Test
    @DisplayName("정상적인 데이터를 전달할 시, 룸메이트 프로필을 생성한다.")
    void shouldCreateRoomMateProfile(){
        // Given
        User user = new User();
        user.setUsername("testUser");
        user.setNickname("testNick");
        user.setPassword("testPassword");
        user.setEmail("testEmail");
        user.setGender(1);
        user.setSchool(new School());

        customUserDetails = new CustomUserDetails(user);  // CustomUserDetails 객체 생성
        RoomMateProfileInputDTO roomMateProfileInputDTO = new RoomMateProfileInputDTO();
        roomMateProfileInputDTO.setCleanPeriod(CleanPeriod.EVERYDAY);
        roomMateProfileInputDTO.setActivityTime(ActivityTime.MORNING);
        roomMateProfileInputDTO.setColdLevel(Level.LOW);
        roomMateProfileInputDTO.setHotLevel(Level.HIGH);
        roomMateProfileInputDTO.setIsSmoking(true);
        roomMateProfileInputDTO.setShowerTime(ShowerTime.THIRTY_TO_FORTY);
        roomMateProfileInputDTO.setHopeDormitories(Set.of("자유관"));
        roomMateProfileInputDTO.setHopeRoomPersons(Set.of(2, 4));
        roomMateProfileInputDTO.setSleepHabits(Set.of(SleepHabit.SNORE));

        RoomMateProfile roomMateProfile = new RoomMateProfile();
        roomMateProfile.setUser(user);
        given(roomMateProfileRepository.save(any(RoomMateProfile.class))).willReturn(null);
        given(hopeDormitoryRepository.save(any())).willReturn(null);
        given(hopeRoomPersonRepository.save(any())).willReturn(null);
        given(ownSleepHabitRepository.save(any())).willReturn(null);

        // When
        roomMateProfileService.createRoomMateProfile(roomMateProfileInputDTO, customUserDetails);

        // Then
        verify(roomMateProfileRepository).save(any(RoomMateProfile.class));
        verify(hopeDormitoryRepository, times(1)).save(any());
        verify(hopeRoomPersonRepository, times(2)).save(any());
        verify(ownSleepHabitRepository).save(any());
    }

    @Test
    @DisplayName("유저가 존재하지 않을 시, 'InvalidTokenUser'예외가 발생한다.")
    void shouldThrowInvalidTokenUserExceptionWhenUserIsNotExist(){
        // Given
        RoomMateProfileInputDTO roomMateProfileInputDTO = new RoomMateProfileInputDTO();
        roomMateProfileInputDTO.setCleanPeriod(CleanPeriod.EVERYDAY);
        roomMateProfileInputDTO.setActivityTime(ActivityTime.MORNING);
        roomMateProfileInputDTO.setColdLevel(Level.LOW);
        roomMateProfileInputDTO.setHotLevel(Level.HIGH);
        roomMateProfileInputDTO.setIsSmoking(true);
        roomMateProfileInputDTO.setShowerTime(ShowerTime.THIRTY_TO_FORTY);
        roomMateProfileInputDTO.setHopeDormitories(Set.of("자유관"));
        roomMateProfileInputDTO.setHopeRoomPersons(Set.of(2, 4));
        roomMateProfileInputDTO.setSleepHabits(Set.of(SleepHabit.SNORE));

        // When
        given(customUserDetails.getUser()).willReturn(null); // 유저가 존재하지 않는 경우

        // Then
        assertThrows(InvalidTokenUser.class, () -> roomMateProfileService.createRoomMateProfile(roomMateProfileInputDTO, customUserDetails));
    }
}