//package com.example.danjamserver.roomMate.service;
//
//import com.example.danjamserver.mate.util.MateFilterValidationService;
//import com.example.danjamserver.roomMate.repository.RoomMateProfileRepository;
//import com.example.danjamserver.user.repository.MyProfileRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("RoomMateCandidateService에서 validate관련 테스트")
//public class RoomMateCandidateValidateTest {
//
//    @Mock
//    MyProfileRepository myProfileRepository;
//
//    @Mock
//    RoomMateProfileRepository roomMateProfileRepository;
//
//    @Mock
//    RoomMateFilteringService roomMateFilteringService;
//    private RoomMateCandidateService roomMateCandidateService=new RoomMateCandidateService(myProfileRepository, roomMateProfileRepository, roomMateFilteringService);
//
//    @Test
//    @DisplayName("입학년도가 2자리가 아닐경우, 입학연도는 2자리로 숫자로 입력해주세요. 메시지를 반환한다.")
//    void testMinEntryYearNotTwoDigits() {
//
//        // Given
//        StringBuilder errorMessage = new StringBuilder();
//
//        // When
//        MateFilterValidationService.validateEntryYear("1", "20", errorMessage);
//
//        // Then
//        assertTrue(errorMessage.toString().contains("입학연도는 2자리 숫자로 입력해주세요."));
//    }
//
//    @Test
//    @DisplayName("입학년도가 숫자가 아닐경우, 입학연도는 2자리로 숫자로 입력해주세요. 메시지를 반환한다.")
//    void testMinEntryYearNotNumber() {
//
//        // Given
//        StringBuilder errorMessage = new StringBuilder();
//
//        // When
//        MateFilterValidationService.validateEntryYear("hi", "20", errorMessage);
//
//        // Then
//        assertTrue(errorMessage.toString().contains("입학연도는 2자리 숫자로 입력해주세요."));
//    }
//
//    @Test
//    @DisplayName("최소 입학년도가 최대입학년도 보다 클경우, 최소 입학연도는 최대입학연도보다 작아야합니다. 메시지를 반환한다.")
//    void whenMinEntryYearBigThanMaxEntryYear() {
//
//        // Given
//        StringBuilder errorMessage = new StringBuilder();
//
//        // When
//        MateFilterValidationService.validateEntryYear("20", "18", errorMessage);
//
//        // Then
//        assertTrue(errorMessage.toString().contains("최소 입학연도는 최대입학연도보다 작아야합니다."));
//    }
//
//    @Test
//    @DisplayName("수면습관 번호가 1~4가 아닐경우, 수면습관은 1~4까지 입력해주세요. 메시지를 반환한다.")
//    void testSleepHabitsNotBetweenOneAndFour() {
//
//        // Given
//        StringBuilder errorMessage = new StringBuilder();
//
//        // When
//        roomMateCandidateService.validateSleepHabits(511, errorMessage);
//
//        // Then
//        assertTrue(errorMessage.toString().contains("수면습관은 1~4까지 입력해주세요."));
//    }
//}
