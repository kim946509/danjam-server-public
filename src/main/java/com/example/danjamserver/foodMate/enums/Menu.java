package com.example.danjamserver.foodMate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Menu {
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    SNACK("분식"),
    DESSERT("디저트");


    private final String menus;
}
