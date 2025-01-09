package com.example.danjamserver.notice.dto;



import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 모바일에서 전달 받은 객체 매핑
 */
@Getter
@Setter
@Builder
@ToString
public class FcmTokenSendDto {


    private String token; // FCM TOKEN
    private String title;
    private String body;
    private String platform; // ios, android
}
