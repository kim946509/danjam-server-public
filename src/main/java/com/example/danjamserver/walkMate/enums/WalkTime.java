package com.example.danjamserver.walkMate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WalkTime {

    HALF_HOUR_WITHIN("30분 이내"),
    HALF_HOUR_ONE("30분 ~ 1시간"),
    ONE_TWO("1시간 ~ 2시간"),
    TWO_OVER("2시간 이상");

    private final String walkTime;

}
