package com.example.danjamserver.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BlockDTO {
    @NotNull(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 32, message = "닉네임은 2자 이상, 32자 이내여야 합니다.")
    String nickname;
}
