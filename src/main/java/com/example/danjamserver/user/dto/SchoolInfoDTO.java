package com.example.danjamserver.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolInfoDTO {
    @NotNull(message = "학교를 입력해주세요.")
    private Long schoolId;
    @NotNull(message = "입학년도를 입력해주세요.")
    private Integer entryYear;
    @NotBlank(message = "전공을 입력해주세요.")
    private String major;
}
