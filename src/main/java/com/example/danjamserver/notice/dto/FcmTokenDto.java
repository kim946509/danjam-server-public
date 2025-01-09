package com.example.danjamserver.notice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FcmTokenDto {
    @NotNull(message = "token을 입력해주세요.")
    String token;
}
