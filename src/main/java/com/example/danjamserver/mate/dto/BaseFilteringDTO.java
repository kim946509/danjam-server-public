package com.example.danjamserver.mate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
public abstract class BaseFilteringDTO {
    String mbti;
    String minBirthYear;
    String maxBirthYear;
    String minEntryYear;
    String maxEntryYear;
    Set<String> colleges;
}
