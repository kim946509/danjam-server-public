package com.example.danjamserver.chatroom.listener;

import com.example.danjamserver.chat.repository.ChatRepository;
import com.example.danjamserver.chatroom.domain.ChatRoom;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.persistence.PostRemove;

@Component
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ChatRoomListener {

    private final ChatRepository chatRepository;

    @PostRemove
    public void onChatRoomRemove(ChatRoom chatRoom) {
        chatRepository.deleteByChatRoomId(chatRoom.getId());
    }
}
