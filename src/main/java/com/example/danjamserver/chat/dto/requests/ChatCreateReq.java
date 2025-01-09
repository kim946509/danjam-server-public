package com.example.danjamserver.chat.dto.requests;

import com.example.danjamserver.chat.domain.ChatType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatCreateReq {

    private String message;
    private ChatType chatType;  // ChatType 필드 추가

    @Builder
    private ChatCreateReq(String message, ChatType chatType) {
        this.message = message;
        this.chatType = chatType;
    }
}
