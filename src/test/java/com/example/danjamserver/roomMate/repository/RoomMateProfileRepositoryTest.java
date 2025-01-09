package com.example.danjamserver.roomMate.repository;

import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.roomMate.domain.RoomMateProfile;
import com.example.danjamserver.roomMate.enums.ActivityTime;
import com.example.danjamserver.roomMate.enums.CleanPeriod;
import com.example.danjamserver.roomMate.enums.Level;
import com.example.danjamserver.roomMate.enums.ShowerTime;
import com.example.danjamserver.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("RoomMateProfileRepository 테스트")
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomMateProfileRepositoryTest {

    @Autowired
    private RoomMateProfileRepository roomMateProfileRepository;

    @Test
    @DisplayName("정상적으로 존재하는 유저의 RoomMateProfile을 조회한다면 해당 RoomMateProfile을 반환한다.")
    void shouldFindRoomMateProfileByUserId() {
        // Given
        School school = new School();
        school.setId(1L);
        school.setName("testSchool");
        school.setKorName("테스트학교");
        User user = User.builder()
                .username("testUser")
                .nickname("testNick")
                .password("testPassword")
                .email("testEmail")
                .gender(1)
                .school(school)
                .build();
        RoomMateProfile profile = RoomMateProfile.builder()
                .isSmoking(true)
                .hotLevel(Level.LOW)
                .coldLevel(Level.LOW)
                .activityTime(ActivityTime.MORNING)
                .cleanPeriod(CleanPeriod.EVERYDAY)
                .showerTime(ShowerTime.THIRTY_TO_FORTY)
                .mateType(MateType.ROOMMATE)
                .user(user)
                .build();
        roomMateProfileRepository.save(profile);

        // When
        RoomMateProfile foundProfile = roomMateProfileRepository.findByUserId(user.getId()).orElse(null);

        // Then
        assertNotNull(foundProfile);

    }

    @Test
    void findProfilesByFilters() {
    }

    @Test
    public void findProfilesByMbtiFilter(){



    }

//    더이상 사용하지 않는 메서드(테스트 작성시 참고용으로 주석처리 함)
//    @Test
//    public void testFindProfileByShowerTimeFilters_whenShowerTimeIsNull() {
//
//        // Given
//        ShowerTime showerTime = null;
//
//        // When
//        List<RoomMateProfile> profileByShowerTimeFilters = roomMateProfileRepository.findProfileByShowerTimeFilters(null);
//
//        // Then
//        assertEquals(4, profileByShowerTimeFilters.size());
//    }
//
//    @Test
//    public void testFindProfileByShowerTimeFilters_whenShowerTimeIs_10_20분() {
//
//        // Given
//        ShowerTime showerTime = ShowerTime._10_20분;
//
//        // When
//        List<RoomMateProfile> profileByShowerTimeFilters = roomMateProfileRepository.findProfileByShowerTimeFilters(showerTime);
//
//        // Then
//        assertEquals(1, profileByShowerTimeFilters.size());
//    }
//
//    @Test
//    public void testFindProfileByShowerTimeFilters_whenShowerTimeIs_20_30분() {
//
//        // Given
//        ShowerTime showerTime = ShowerTime._20_30분;
//
//        // When
//        List<RoomMateProfile> profileByShowerTimeFilters = roomMateProfileRepository.findProfileByShowerTimeFilters(showerTime);
//
//        // Then
//        assertEquals(2, profileByShowerTimeFilters.size());
//    }
//
//    @Test
//    public void testFindProfileByShowerTimeFilters_whenShowerTimeIs_30_40분() {
//
//        // Given
//        ShowerTime showerTime = ShowerTime._30_40분;
//
//        // When
//        List<RoomMateProfile> profileByShowerTimeFilters = roomMateProfileRepository.findProfileByShowerTimeFilters(showerTime);
//
//        // Then
//        assertEquals(3, profileByShowerTimeFilters.size());
//    }
//
//    @Test
//    public void testFindProfileByShowerTimeFilters_whenShowerTimeIs_40분() {
//
//        // Given
//        ShowerTime showerTime = ShowerTime._40분;
//
//        // When
//        List<RoomMateProfile> profileByShowerTimeFilters = roomMateProfileRepository.findProfileByShowerTimeFilters(showerTime);
//
//        // Then
//        assertEquals(4, profileByShowerTimeFilters.size());
//    }
//
//    @BeforeEach
//    public void setUp() {
//
//        //공통적으로 사용되는 RoomMAtePRofile 객체 생성
//        RoomMateProfile profile1 = RoomMateProfile.builder()
//                .showerTime(ShowerTime._10_20분)
//                .isSmoking(true)
//                .hotLevel(Level.상관없어요)
//                .coldLevel(Level.상관없어요)
//                .activityTime(ActivityTime.상관없어요)
//                .cleanPeriod(CleanPeriod.상관없어요)
//                .build();
//        roomMateProfileRepository.save(profile1);
//
//        RoomMateProfile profile2 = RoomMateProfile.builder()
//                .showerTime(ShowerTime._20_30분)
//                .isSmoking(true)
//                .hotLevel(Level.상관없어요)
//                .coldLevel(Level.상관없어요)
//                .activityTime(ActivityTime.상관없어요)
//                .cleanPeriod(CleanPeriod.상관없어요)
//                .build();
//        roomMateProfileRepository.save(profile2);
//
//        RoomMateProfile profile3 = RoomMateProfile.builder()
//                .showerTime(ShowerTime._30_40분)
//                .isSmoking(true)
//                .hotLevel(Level.상관없어요)
//                .coldLevel(Level.상관없어요)
//                .activityTime(ActivityTime.상관없어요)
//                .cleanPeriod(CleanPeriod.상관없어요)
//                .build();
//        roomMateProfileRepository.save(profile3);
//
//        RoomMateProfile profile4 = RoomMateProfile.builder()
//                .showerTime(ShowerTime._40분)
//                .isSmoking(true)
//                .hotLevel(Level.상관없어요)
//                .coldLevel(Level.상관없어요)
//                .activityTime(ActivityTime.상관없어요)
//                .cleanPeriod(CleanPeriod.상관없어요)
//                .build();
//        roomMateProfileRepository.save(profile4);
//    }
}