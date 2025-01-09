package com.example.danjamserver.workoutMate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkoutTime {

    ONE_WITHIN("한시간 이내"),
    ONE_TWO("1시간 ~ 2시간"),
    TWO_THREE("2시간 ~ 3시간"),
    THREE_OVER("3시간 이상");

    private final String workoutTime;

}
