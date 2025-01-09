package com.example.danjamserver.user.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {
    INAPPROPRIATE_PROFILE("부적절한 내용의 프로필"),
    INAPPROPRIATE_CHAT("부적절한 내용의 채팅"),
    FALSE_INFORMATION("허위정보"),
    HARASSMENT("지속적인 괴롭힘");

    private final String reportTypes;

    @JsonValue
    public String toValue() {
        return reportTypes;  // Enum의 `reportTypes` 값을 반환
    }
}