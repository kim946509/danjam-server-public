package com.example.danjamserver.walkMate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WalkIntensity {

    LOW("여유롭게 산책하고 싶어요"),
    MIDDLE("활동적인 산책을 하고 싶어요"),
    HIGH("땀이 날 정도로 운동이 됐으면 좋겠어요");

    private final String walkIntensity;

}
