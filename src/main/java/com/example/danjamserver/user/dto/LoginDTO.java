package com.example.danjamserver.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotBlank(message = "유저명을 입력해주세요.")
    @Size(min = 2, max = 32, message = "유저명은 2자 이상, 32자 이내여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입혁해주세요.")
    @Size(min = 8, max = 32, message = "비밀번호는 8자 이상, 32자 이내여야 합니다.")
    private String password;
}
