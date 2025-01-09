package com.example.danjamserver.foodMate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MealTime {
    FIFTEEN_MINUTES("15분"),
    THIRTY_MINUTES("30분"),
    OVER_AN_HOUR("1시간 이상");
    private final String mealTime;
}
