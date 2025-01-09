package com.example.danjamserver.mate.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
@DisplayName("모든 필터의 CandidateService에서 공통 validate관련 테스트")
class MateFilterValidationServiceTest {

    @Test
    @DisplayName("성별이 0, 1이 아닐경우, 성별은 0, 1로 입력해주세요. 메시지를 반환한다.")
    void whenGenderNotZeroOrOne() {

        // Given
        StringBuilder errorMessage = new StringBuilder();

        // When
        MateFilterValidationService.validateGender(Set.of(0,1,2), errorMessage);

        // Then
        assertTrue(errorMessage.toString().contains("성별은 0, 1로 입력해주세요."));
    }

    @Test
    @DisplayName("성별이 0, 1일경우, errorMessage는 비어있다.")
    void whenGenderIsZeroOrOne() {

        // Given
        StringBuilder errorMessage = new StringBuilder();

        // When
        MateFilterValidationService.validateGender(Set.of(0,1), errorMessage);

        // Then
        assertTrue(errorMessage.toString().isEmpty());
    }
}