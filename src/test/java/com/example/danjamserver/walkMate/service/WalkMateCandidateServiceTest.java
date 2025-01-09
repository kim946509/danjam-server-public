package com.example.danjamserver.walkMate.service;

import com.example.danjamserver.mate.util.MateFilterUtilService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.CustomValidationException;
import com.example.danjamserver.walkMate.dto.WalkMateCandidateDetailDTO;
import com.example.danjamserver.walkMate.dto.WalkMateFilteringDTO;
import com.example.danjamserver.walkMate.enums.WalkIntensity;
import com.example.danjamserver.walkMate.enums.WalkTime;
import com.example.danjamserver.walkMate.enums.WalkTimeZone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("WalkMateCandidateService 테스트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WalkMateCandidateServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalkMateCandidateService walkMateCandidateService;

    @MockBean
    private CustomUserDetails customUserDetails;

    @Autowired
    MateFilterUtilService mateFilterUtilService;

    @Test
    @DisplayName("정상적으로 WalkMateCandidate를 조회한다면 10개의 WalkMateCandidateDetailDTO를 랜덤으로 반환한다.")
    void shouldGetWalkMateCandidateList() {
        // Given
        WalkMateFilteringDTO walkMateFilteringDTO = WalkMateFilteringDTO.builder()
                .mbti("ij")
                .minBirthYear("95")
                .maxBirthYear("20")
                .minEntryYear("15")
                .maxEntryYear("25")
                .colleges(Set.of("과학기술대학"))
                .gender(Set.of(1))
                .preferredWalkTimeZones(Set.of(WalkTimeZone.MORNING))
                .preferredWalkIntensities(Set.of(WalkIntensity.LOW))
                .preferredWalkTimes(Set.of(WalkTime.HALF_HOUR_ONE))
                .build();

        // When
        Set<WalkMateCandidateDetailDTO> walkMateCandidates = walkMateCandidateService.getWalkMateCandidates(customUserDetails, walkMateFilteringDTO, 10);
        // Then
        // 결과가 10개인지 확인
        assertThat(walkMateCandidates).isNotNull();
        assertThat(walkMateCandidates.size()).isEqualTo(10);
        for (WalkMateCandidateDetailDTO walkMateCandidateDetailDTO : walkMateCandidates) {
            // walkMateCandidateDetailDTO들의 모든 mbti가 "ij"를 포함하는지 확인. ex) infj
            assertThat(walkMateCandidateDetailDTO.getMbti()).contains("i").contains("j");
            // walkMateCandidateDetailDTO들의 모든 생년월일 앞 2자리가 95 이상  또는 20 미만인지 확인
            int birth = Integer.parseInt(walkMateCandidateDetailDTO.getBirth().substring(0, 2));
            boolean isBirthValid = birth >= 95 || birth < 20;
            assertThat(isBirthValid).isTrue();
            // walkMateCandidateDetailDTO들의 모든 입학년도가 2015 이상 2025 미만인지 확인
            assertThat(walkMateCandidateDetailDTO.getEntryYear()).isGreaterThanOrEqualTo(2015);
            assertThat(walkMateCandidateDetailDTO.getEntryYear()).isLessThan(2025);
            // 단과대 이름이 "과학기술대학"인지 확인
            String collegeByMajor = mateFilterUtilService.getCollegeByMajor(getUser().getSchool().getId(), walkMateCandidateDetailDTO.getMajor());
            assertThat(collegeByMajor).isEqualTo("과학기술대학");

            // 성별이 1(남자)인지 확인
            assertThat(walkMateCandidateDetailDTO.getGender()).isEqualTo(1);

            // 희망 걷기 시간대가 "MORNING"인지 확인
            assertThat(walkMateCandidateDetailDTO.getPreferredWalkTimeZones().contains(WalkTimeZone.MORNING)).isTrue();

            // 희망 걷기 강도가 "LOW"인지 확인
            assertThat(walkMateCandidateDetailDTO.getPreferredWalkIntensities().contains(WalkIntensity.LOW)).isTrue();

            // 희망 걷기 시간이 "HALF_HOUR_ONE"인지 확인
            assertThat(walkMateCandidateDetailDTO.getPreferredWalkTime().equals(WalkTime.HALF_HOUR_ONE)).isTrue();
        }
    }

    @Test
    @DisplayName("mbti가 null일 경우, CustomValidationException 예외를 반환한다.")
    void shouldThrowCustomValidationExceptionWhenMbtiIsNull() {
        // Given
        WalkMateFilteringDTO walkMateFilteringDTO = WalkMateFilteringDTO.builder()
                .mbti(null)
                .build();
        // When

        // Then
        Assertions.assertThrows(CustomValidationException.class, () -> {
            walkMateCandidateService.getWalkMateCandidates(customUserDetails, walkMateFilteringDTO, 10);
        });
    }

    @Test
    @DisplayName("Walk필터링 선택지들이 모두 null일 경우에도 10개의 WalkMateCandidateDetailDTO를 반환한다.")
    void shouldGetWalkMateCandidateListWhenAdditionalFilteringIsNull() {
        // Given
        WalkMateFilteringDTO walkMateFilteringDTO = WalkMateFilteringDTO.builder()
                .mbti("ij")
                .minBirthYear("95")
                .maxBirthYear("20")
                .minEntryYear("15")
                .maxEntryYear("25")
                .colleges(Set.of("과학기술대학"))
                .preferredWalkIntensities(null)
                .preferredWalkTimes(null)
                .preferredWalkTimeZones(null)
                .build();
        // When
        Set<WalkMateCandidateDetailDTO> walkMateCandidates = walkMateCandidateService.getWalkMateCandidates(customUserDetails, walkMateFilteringDTO, 10);

        // Then
        // 결과가 10개인지 확인
        assertThat(walkMateCandidates).isNotNull();
        assertThat(walkMateCandidates.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("성별이 3개 이상일 경우, CustomValidationException 예외를 반환한다. message = '성별은 0, 1로 입력해주세요. '")
    void shouldThrowCustomValidationExceptionWhenGenderSizeIsOverTwo() {
        // Given
        WalkMateFilteringDTO walkMateFilteringDTO = WalkMateFilteringDTO.builder()
                .mbti("ij")
                .gender(Set.of(0, 1, 2))
                .build();

        // When&Then
        CustomValidationException customValidationException = Assertions.assertThrows(CustomValidationException.class, () -> {
            walkMateCandidateService.getWalkMateCandidates(customUserDetails, walkMateFilteringDTO, 10);
        });
        assertThat(customValidationException.getMessage()).isEqualTo("성별은 0, 1로 입력해주세요. ");
    }
    @BeforeEach
    void setUp() {
        when(customUserDetails.getUser()).thenReturn(getUser());
    }

    // 유저 데이터를 반환하는 메소드
    private User getUser() {
        User user = userRepository.findByUsername("test0004").get();
        return user;
    }
}
