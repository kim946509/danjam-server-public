package com.example.danjamserver.studyMate.enums;

import com.example.danjamserver.util.exception.InvalidInputException;
import com.example.danjamserver.util.exception.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyTime {

    WITHIN_ONE("1시간 이내"),
    ONE_TWO("1시간 ~ 2시간"),
    TWO_THREE("2시간 ~ 3시간"),
    THREE_OVER("3시간 ~");

    private final String studyTime;

    // 문자열을 기반으로 StudyTime Enum을 반환하는 메서드
    public static StudyTime fromString(String text) {
        for (StudyTime st : StudyTime.values()) {
            if (st.getStudyTime().equals(text)) {
                return st;
            }
        }
        // 잘못된 입력이 들어왔을 때 예외 처리
        throw new InvalidInputException(ResultCode.NOT_READABLE_MESSAGE);
    }

    // Enum을 기반으로 문자열을 반환하는 메서드
    public static String getSentence(StudyTime studyTime) {
        return studyTime.getStudyTime();
    }
}
