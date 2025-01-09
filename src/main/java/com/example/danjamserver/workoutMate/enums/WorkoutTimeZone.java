package com.example.danjamserver.workoutMate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkoutTimeZone {

    DAWN("상쾌한 공기의 새벽이 좋아요"),
    MORNING("모두들 바삐 움직이는 아침이 좋아요"),
    LUNCH("따듯한 햇빛과 함께하는 점심이 좋아요"),
    DINNER("여유로운 저녁이 좋아요");

    private final String workoutTimeZone;

}
