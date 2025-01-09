package com.example.danjamserver.chatroom.dto.responses;

import com.example.danjamserver.chat.domain.Chat;
import com.example.danjamserver.chatroom.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRoomListAndLastChatRes {

    private Long chatRoomId;
    private String title;
    private String chatRoomMateType;
    private String lastChat;
    private LocalDateTime lastChatTime;
    private int unreadCount; // 추가: 읽지 않은 메시지 수 필드

    @Builder
    public ChatRoomListAndLastChatRes(Long chatRoomId, String title, String chatRoomMateType,
                                      String lastChat, LocalDateTime lastChatTime, int unreadCount) {
        this.chatRoomId = chatRoomId;
        this.title = title;
        this.chatRoomMateType = chatRoomMateType;
        this.lastChat = lastChat;
        this.lastChatTime = lastChatTime;
        this.unreadCount = unreadCount;  // 읽지 않은 메시지 수 설정
    }

    public static ChatRoomListAndLastChatRes create(ChatRoom chatRoom, Chat chat, int unreadCount) {
        return ChatRoomListAndLastChatRes.builder()
                .chatRoomId(chatRoom.getId())
                .title(chatRoom.getTitle())
                .chatRoomMateType(chatRoom.getChatRoomMateType().toString())
                .lastChat(chat != null ? chat.getMessage() : "No messages")
                .lastChatTime(chat != null ? chat.getCreatedDateTime() : null)
                .unreadCount(unreadCount)  // 읽지 않은 메시지 수 설정
                .build();
    }
}
