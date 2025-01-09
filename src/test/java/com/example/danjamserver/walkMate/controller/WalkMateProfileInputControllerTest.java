package com.example.danjamserver.walkMate.controller;

import com.example.danjamserver.annotation.WithMockCustomUser;
import com.example.danjamserver.springSecurity.role.Role;
import com.example.danjamserver.walkMate.dto.WalkMateProfileInputDTO;
import com.example.danjamserver.walkMate.enums.WalkIntensity;
import com.example.danjamserver.walkMate.enums.WalkTime;
import com.example.danjamserver.walkMate.enums.WalkTimeZone;
import com.example.danjamserver.walkMate.service.WalkMateCandidateService;
import com.example.danjamserver.walkMate.service.WalkMateProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Set;


@WebMvcTest(controllers = WalkMateController.class)
@WithMockCustomUser(username = "testmate",grade = "STRANGER")
//@WithMockUser(username = "testmate",roles = "STRANGER")
@DisplayName("WalkMateProfileInputController 테스트")
public class WalkMateProfileInputControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalkMateProfileService walkMateProfileService;

    @MockBean
    private WalkMateCandidateService walkMateCandidateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자가 정상적인 데이터를 입력시, WalkMateProfile을 생성할 수 있다.")
    void shouldCreateWalkMateProfile() throws Exception {
        // Given
        WalkMateProfileInputDTO walkMateProfileInputDTO = new WalkMateProfileInputDTO();
        walkMateProfileInputDTO.setPreferredWalkTime(WalkTime.ONE_TWO);
        walkMateProfileInputDTO.setPreferredWalkTimeZones(Set.of(WalkTimeZone.MORNING,WalkTimeZone.LUNCH));
        walkMateProfileInputDTO.setPreferredWalkIntensities(Set.of(WalkIntensity.LOW,WalkIntensity.MIDDLE));

        // When & Then
        mockMvc.perform(post("/api/mate/walkmate/profile")  // 올바른 엔드포인트
                        .with(csrf()) // CSRF 토큰 비활성화
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walkMateProfileInputDTO)))  // JSON 변환 후 요청 본문에 추가
                .andExpect(status().isOk());  // 상태 코드가 200 OK인지 확인
    }

    @Test
    @DisplayName("사용자가 preferredWalkTimeZone을 null로 입력시, '희망 걷기 시간대를 선택해주세요.'message를 반환한다.")
    void shouldReturnCode100AndMessageWhenPreferredWalkTimeZoneIsNull() throws Exception {
        // Given
        WalkMateProfileInputDTO walkMateProfileInputDTO = new WalkMateProfileInputDTO();
        walkMateProfileInputDTO.setPreferredWalkTime(WalkTime.ONE_TWO);
        walkMateProfileInputDTO.setPreferredWalkTimeZones(null);
        walkMateProfileInputDTO.setPreferredWalkIntensities(Set.of(WalkIntensity.LOW,WalkIntensity.MIDDLE));

        // When & Then
        mockMvc.perform(post("/api/mate/walkmate/profile")  // 올바른 엔드포인트
                        .with(csrf()) // CSRF 토큰 비활성화
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walkMateProfileInputDTO)))  // JSON 변환 후 요청 본문에 추가
                .andExpect(status().isBadRequest())  // 상태 코드가 400 BAD_REQUEST인지 확인
                .andExpect(jsonPath("$.message").value("잘못된 입력값이 존재합니다. : 희망 걷기 시간대를 선택해주세요."));// 메시지 확인
    }
}
