package com.example.danjamserver.roomMate.service;

import com.example.danjamserver.roomMate.domain.RoomMateProfile;
import com.example.danjamserver.roomMate.dto.RoomMateFilteringDTO;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("RoomMateFileringService 테스트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoomMateFilteringServiceTest {

    @Autowired
    private RoomMateFilteringService roomMateFilteringService;

    @Autowired
    private UserRepository userRepository;

    private Pageable pageable;

    @Test
    @DisplayName("같은 필터링 조건으로 후보자를 두 번 조회하면 캐시에서 조회한다.")
    void shouldGetFilteredCandidates() {

        // Given
        RoomMateFilteringDTO roomMateFilteringDTO = RoomMateFilteringDTO.builder()
                .mbti("in")
                .build();
        Long schoolId = 1L;
        Integer gender = 1;

        // When
        List<RoomMateProfile> filteredCandidates = roomMateFilteringService.getFilteredCandidates(roomMateFilteringDTO, schoolId, gender, null, pageable);
        List<RoomMateProfile> filteredCandidates2 = roomMateFilteringService.getFilteredCandidates(roomMateFilteringDTO, schoolId, gender, null, pageable);

        // Then
        // 캐시에서 조회되므로 두 번 조회해도 같은 객체를 반환한다.
        assertThat(filteredCandidates).isEqualTo(filteredCandidates2);

    }

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }



}
