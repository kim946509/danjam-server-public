package com.example.danjamserver.chatroom.service;

import com.example.danjamserver.chat.domain.Chat;
import com.example.danjamserver.chat.repository.ChatRepository;
import com.example.danjamserver.chatroom.domain.ChatRoom;
import com.example.danjamserver.chatroom.dto.responses.ChatRoomListAndLastChatRes;
import com.example.danjamserver.chatroom.repository.ChatRoomUserRepository;
import com.example.danjamserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomQueryService {

    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRepository chatRepository;

    public List<ChatRoomListAndLastChatRes> getUserChatRooms(User user) {
        List<ChatRoom> chatRooms = chatRoomUserRepository.findChatRoomsByUsername(user.getUsername());
        Map<ChatRoom, Chat> lastMessages = new HashMap<>();

        for (ChatRoom chatRoom : chatRooms) {
            // 각 채팅방의 마지막 메시지를 가져옵니다.
            List<Chat> messages = chatRepository.findLastMessageByChatRoomId(chatRoom.getId());
            Chat lastChatMessage = messages.isEmpty() ? null : messages.get(0);
            lastMessages.put(chatRoom, lastChatMessage);
        }

        return chatRooms.stream()
                .map(chatRoom -> {
                    // 해당 채팅방에서 현재 유저가 읽지 않은 메시지 수를 계산
                    int unreadCount = chatRepository.findByChatRoomIdAndUnreadMembersContains(chatRoom.getId(), user.getUsername()).size();

                    // ChatRoomListAndLastChatRes DTO 생성 시 읽지 않은 메시지 수 포함
                    return ChatRoomListAndLastChatRes.create(chatRoom, lastMessages.get(chatRoom), unreadCount);
                })
                .collect(Collectors.toList());
    }
}
