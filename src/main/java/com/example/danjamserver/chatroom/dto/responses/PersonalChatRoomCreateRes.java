package com.example.danjamserver.chatroom.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonalChatRoomCreateRes {

    private String title;
    private Long chatRoomId;
    private String chatRoomMateType;

    @Builder
    public PersonalChatRoomCreateRes(String title, String chatRoomMateType, Long chatRoomId) {
        this.title = title;
        this.chatRoomMateType = chatRoomMateType;
        this.chatRoomId = chatRoomId;
    }

    public static PersonalChatRoomCreateRes create(Long chatRoomId, String title, String chatRoomMateType) {
        return PersonalChatRoomCreateRes.builder()
                .chatRoomId(chatRoomId)
                .title(title)
                .chatRoomMateType(chatRoomMateType)
                .build();
    }
}
