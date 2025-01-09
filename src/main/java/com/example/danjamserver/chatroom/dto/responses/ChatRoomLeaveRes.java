package com.example.danjamserver.chatroom.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomLeaveRes {
    private String title;
    private Long chatRoomId;
    private String chatRoomMateType;

    @Builder
    ChatRoomLeaveRes(String title, Long chatRoomId, String chatRoomMateType) {
        this.title = title;
        this.chatRoomId = chatRoomId;
        this.chatRoomMateType = chatRoomMateType;
    }

    public static ChatRoomLeaveRes create(String title, Long chatRoomId, String chatRoomMateType) {
        return ChatRoomLeaveRes.builder()
                .title(title)
                .chatRoomId(chatRoomId)
                .chatRoomMateType(chatRoomMateType)
                .build();
    }
}
