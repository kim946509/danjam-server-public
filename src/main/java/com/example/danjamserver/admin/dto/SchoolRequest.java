package com.example.danjamserver.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SchoolRequest {
    @NotNull
    private String schoolName;

    @NotNull
    private String schoolKorName;
}