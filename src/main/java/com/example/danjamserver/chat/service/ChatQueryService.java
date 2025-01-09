package com.example.danjamserver.chat.service;

import com.example.danjamserver.chat.domain.Chat;
import com.example.danjamserver.chat.dto.responses.ChatMessageReadAckRes;
import com.example.danjamserver.chat.dto.responses.ChatMessageRes;
import com.example.danjamserver.chat.repository.ChatRepository;
import com.example.danjamserver.chatroom.repository.ChatRoomUserRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.exception.ForbiddenAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatQueryService {

    private final ChatRepository chatRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // 채팅 로그를 조회하고 읽음 처리 후 반환
    public List<ChatMessageRes> getChatLogsAndMarkAsRead(Long chatRoomId, CustomUserDetails userDetails) {
        log.info("getChatLogsAndMarkAsRead called with chatRoomId: {}, user: {}", chatRoomId, userDetails.getUsername());

        // 채팅방 멤버인지 확인
        validateUserInChatRoom(chatRoomId, userDetails.getId());

        // 읽지 않은 메시지들 읽음 처리
        markUnreadMessagesAsRead(chatRoomId, userDetails.getUsername());

        // 채팅 로그를 가져와 반환
        List<ChatMessageRes> chatLogs = chatRepository.findByChatRoomId(chatRoomId)
                .stream()
                .map(ChatMessageRes::create)
                .collect(Collectors.toList());

        log.info("getChatLogsAndMarkAsRead completed for chatRoomId: {}", chatRoomId);
        return chatLogs;
    }

    // 사용자가 채팅방 멤버인지 확인
    private void validateUserInChatRoom(Long chatRoomId, Long userId) {
        boolean isMember = chatRoomUserRepository.existsByChatRoomIdAndUserId(chatRoomId, userId);
        if (!isMember) {
            log.warn("User ID {} is not a member of chatRoomId {}", userId, chatRoomId);
            throw new ForbiddenAccessException();
        }
        log.info("User ID {} is a member of chatRoomId {}", userId, chatRoomId);
    }

    // 읽지 않은 메시지를 읽음 처리하고 상태 전송
    private void markUnreadMessagesAsRead(Long chatRoomId, String username) {
        List<Chat> unreadChats = chatRepository.findByChatRoomIdAndUnreadMembersContains(chatRoomId, username);
        if (unreadChats.isEmpty()) {
            log.info("No unread messages for user: {} in chatRoomId: {}", username, chatRoomId);
            return;
        }

        // 메시지 읽음 처리 및 상태 전송
        unreadChats.forEach(chat -> {
            chat.getUnreadMembers().remove(username);
            chatRepository.save(chat);
            int unreadCount = chat.getUnreadMembers().size();
            log.info("Marked message as read with messageId: {}, unread count: {} for user: {}", chat.getId(), unreadCount, username);

            // 각 메시지의 읽음 상태를 클라이언트에 전송
            ChatMessageReadAckRes readAck = new ChatMessageReadAckRes(chat.getId(), unreadCount);
            messagingTemplate.convertAndSend("/topic/chat-rooms." + chatRoomId + ".ack", readAck);
            log.info("Read acknowledgement sent for messageId: {} to topic: /topic/chat-rooms.{}.ack", chat.getId(), chatRoomId);
        });

        log.info("Marked {} messages as read for user: {} in chatRoomId: {}", unreadChats.size(), username, chatRoomId);
    }
}
