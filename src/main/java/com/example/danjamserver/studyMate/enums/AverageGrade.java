package com.example.danjamserver.studyMate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AverageGrade {

    UNDER_TWO("~2.0"),
    TWO_THREE("2.0 ~ 3.0"),
    THREE_FOUR("3.0 ~ 4.0"),
    FOUR_OVER("4.0 ~ 4.5"),
    NOT_PUBLIC("공개하지 않을래요");

    private final String grade;

    // 문자열을 기반으로 GradeEverage Enum을 반환하는 메서드
    public static AverageGrade fromString(String text) {
        for (AverageGrade ge : AverageGrade.values()) {
            if (ge.getGrade().equals(text)) {
                return ge;
            }
        }
        return null;
    }

    // Enum을 기반으로 문자열을 반환하는 메서드
    public static String getSentence(AverageGrade averageGrade) {
        return averageGrade.getGrade();
    }
}
