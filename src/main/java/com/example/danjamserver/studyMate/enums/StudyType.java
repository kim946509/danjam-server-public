package com.example.danjamserver.studyMate.enums;

import com.example.danjamserver.util.exception.InvalidInputException;
import com.example.danjamserver.util.exception.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyType {
    MAJOR("전공과목 공부"),
    CERTIFICATION("자격증 공부"),
    LANGUAGE("어학 공부"),
    EXAM("고시 공부"),
    JOB_PREPARATION("취업 준비"),
    PROGRAMING("프로그래밍"),
    HOBBY_DEVELOPMENT("취미/자기계발"),
    FREE("자율");

    private final String studyType;

    // 문자열을 기반으로 studyType Enum을 반환하는 메서드
    public static StudyType fromString(String text) {
        for (StudyType st : StudyType.values()) {
            if (st.getStudyType().equals(text)) {
                return st;
            }
        }
        // 잘못된 입력이 들어왔을 때 예외 처리
        throw new InvalidInputException(ResultCode.NOT_READABLE_MESSAGE);
    }

    // Enum을 기반으로 문자열을 반환하는 메서드
    public static String getSentence(StudyType studyType) {
        return studyType.getStudyType();
    }
}
