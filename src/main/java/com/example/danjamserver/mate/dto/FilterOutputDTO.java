package com.example.danjamserver.mate.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class FilterOutputDTO {
    String schoolName;
    Set<String> collegeNames;
    JsonNode dormitoryInfo;
    Integer gender;
}
