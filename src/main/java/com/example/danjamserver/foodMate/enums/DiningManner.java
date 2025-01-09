package com.example.danjamserver.foodMate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiningManner {
    CRITICAL("아주중요"),
    MODERATE("어느정도"),
    INDIFFERENT("신경안씀");

    private final String diningManner;
}
