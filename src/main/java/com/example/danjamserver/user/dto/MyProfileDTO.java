package com.example.danjamserver.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyProfileDTO {
    @Pattern(regexp = "^\\d{6}$", message = "생년월일은 6자리 숫자여야 합니다.")
    private String birth;

    private Integer entryYear;
    private String major;

    @Pattern(regexp = "^(i|e)(n|s)(f|t)(p|j)$", message = "존재하지 않는 MBTI 유형입니다.")
    private String mbti;
    @Size(max = 40, message = "소개글은 40자 이내여야 합니다.")
    private String greeting;
}
