package com.example.danjamserver.chat.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageReadAckRes {

    private String messageId;
    private int unreadCount;  // 남은 안읽은 인원 수
}
