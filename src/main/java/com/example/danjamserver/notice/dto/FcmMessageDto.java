package com.example.danjamserver.notice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * FCM 전송 포캣 DTO
 */
@Getter
@Builder
public class FcmMessageDto {
    private boolean validateOnly;
    private FcmMessageDto.Message message;


    /**
     * return { "message":{ "token":"token", "notification":{ "body":"body", "title":"Message title" } } }
     */
    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private FcmMessageDto.Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Data {
        private String name;
        private String description;
    }
}
