package com.example.danjamserver.chatroom.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupChatRoomCreateRes {

    private Long chatRoomId;
    private String title;
    private String chatRoomMateType;

    @Builder
    private GroupChatRoomCreateRes(String title, String chatRoomMateType, Long chatRoomId) {
        this.title = title;
        this.chatRoomMateType = chatRoomMateType;
        this.chatRoomId = chatRoomId;
    }

    public static GroupChatRoomCreateRes create(Long chatRoomId, String title, String chatRoomMateType) {
        return GroupChatRoomCreateRes.builder()
                .chatRoomId(chatRoomId)
                .title(title)
                .chatRoomMateType(chatRoomMateType)
                .build();
    }
}
