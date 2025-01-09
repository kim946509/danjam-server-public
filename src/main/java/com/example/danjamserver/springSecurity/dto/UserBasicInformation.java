package com.example.danjamserver.springSecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//로그인 성공시에 유저의 기본정보를 반환하는 DTO
public class UserBasicInformation {

    private String nickname;
    private Integer gender;
    private String role;
    private String korSchoolName;
    private String major;
}
