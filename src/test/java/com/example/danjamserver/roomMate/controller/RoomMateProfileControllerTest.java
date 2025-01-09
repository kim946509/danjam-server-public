package com.example.danjamserver.roomMate.controller;

import com.example.danjamserver.roomMate.dto.RoomMateProfileInputDTO;
import com.example.danjamserver.roomMate.enums.ActivityTime;
import com.example.danjamserver.roomMate.enums.CleanPeriod;
import com.example.danjamserver.roomMate.enums.Level;
import com.example.danjamserver.roomMate.enums.ShowerTime;
import com.example.danjamserver.roomMate.enums.SleepHabit;
import com.example.danjamserver.roomMate.service.RoomMateProfileService;
import com.example.danjamserver.springSecurity.role.Role;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoomMateProfileController.class)
class RoomMateProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomMateProfileService roomMateProfileService;

    @MockBean
    private UserRepository userRepository;  // UserRepository도 Mock으로 설정

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("룸메이트 프로필 생성 성공 테스트")
//    @WithUserDetails(value = "testUser", userDetailsServiceBeanName = "customUserDetailsService")
    void shouldCreateRoomMateProfile() throws Exception {
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

        String json = objectMapper.writeValueAsString(roomMateProfileInputDTO);

        // UserRepository Mock 설정
        User mockUser = new User();  // User 객체를 생성하고 필요한 값을 설정
        mockUser.setRole(Role.AUTH_USER);
        mockUser.setUsername("testUser");
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));


        // When
        Mockito.doNothing().when(roomMateProfileService).createRoomMateProfile(Mockito.any(), Mockito.any());

        // Then
        mockMvc.perform(post("/api/mate/roommate/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())  // 200 상태 코드 확인
                .andExpect(jsonPath("$.status").value("OK"))  // 응답 JSON의 status 필드 확인
                .andExpect(jsonPath("$.code").value(0))  // 응답 JSON의 code 필드 확인
                .andExpect(jsonPath("$.message").value("성공적으로 처리되었습니다."))  // 응답 JSON의 message 필드 확인
                .andExpect(jsonPath("$.data").value("프로필이 입력되었습니다."));  // 응답 JSON의 data 필드 확인
    }
}