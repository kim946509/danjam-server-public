package com.example.danjamserver.workoutMate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkoutIntensity {

    LIGHT_WORKOUT("가벼운 운동이 좋아요"),
    SWEAT_WORKOUT("어느 정도 땀을 내고 싶어요"),
    MUSCLE_WORKOUT("근육에 자극이 갈 정도로 하고 싶어요"),
    ANYWAY("상관없어요!");

    private final String workoutIntensity;

}
