package com.example.danjamserver.chat.dto.responses;

import com.example.danjamserver.chat.domain.Chat;
import com.example.danjamserver.chat.domain.ChatType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRes {

    private String id;
    private Long chatRoomId;
    private String sender;
    private String message;
    private String timestamp;
    private ChatType chatType;
    private int unreadCount; // 추가: 안읽은 인원수 필드

    @Builder
    public ChatMessageRes(String id, Long chatRoomId, String sender, String message, String timestamp, ChatType chatType, int unreadCount) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
        this.chatType = chatType;
        this.unreadCount = unreadCount; // 안읽은 인원수 설정
    }

    public static ChatMessageRes create(Chat chat) {
        return ChatMessageRes.builder()
                .id(chat.getId())
                .chatRoomId(chat.getChatRoomId())
                .sender(chat.getSender())
                .message(chat.getMessage())
                .timestamp(chat.getCreatedDateTime().toString())
                .chatType(chat.getChatType())
                .unreadCount(chat.getUnreadMembers().size())  // 안읽은 인원수 계산
                .build();
    }
}
