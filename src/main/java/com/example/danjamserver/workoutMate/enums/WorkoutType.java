package com.example.danjamserver.workoutMate.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkoutType {

    ANYWAY("상관없어요"),
    AN_AEROBIC("무산소운동"),
    AEROBIC("유산소운동"),
    STRENGTH_TRAINING("근력운동"),
    GROUP_EXERCISE("단체 운동"),
    RACKET_EXERCISE("라켓 운동(2인)");

    private final String workoutType;

}
