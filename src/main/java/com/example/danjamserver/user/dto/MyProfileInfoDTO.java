package com.example.danjamserver.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 직렬화에서 제외
public class MyProfileInfoDTO {
    private String profileImgUrl; //이미지
    private String nickname; //닉네임
    private String birth; //생년월일
    private String major; //전공
    private String majorState;
    private String greeting; //소개글
    private String greetingState;

    //태그 정보
    private String mbti; // mbti
    private String mbtiState;
    private Integer gender; //성별
    private String genderState;
}
