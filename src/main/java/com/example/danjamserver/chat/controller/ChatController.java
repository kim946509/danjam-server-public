package com.example.danjamserver.chat.controller;

import com.example.danjamserver.chat.dto.requests.ChatCreateReq;
import com.example.danjamserver.chat.dto.responses.ChatMessageRes;
import com.example.danjamserver.chat.service.ChatCommandService;
import com.example.danjamserver.chat.service.ChatQueryService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatCommandService chatCommandService;
    private final ChatQueryService chatQueryService;


    @MessageMapping("chat-rooms.{chatRoomId}")
    public void sendMessage(@DestinationVariable Long chatRoomId,
                            @Payload ChatCreateReq chatCreateReq,
                            StompHeaderAccessor accessor) {
        CustomUserDetails userDetails = Optional.ofNullable((CustomUserDetails) accessor.getSessionAttributes().get("User"))
                .orElseThrow(() -> new IllegalStateException("User session not found"));
        log.info("[ChatController - sendMessage] Called - chatRoomId: {}, user: {}", chatRoomId, userDetails.getUsername());
        chatCommandService.sendMessage(chatRoomId, chatCreateReq, userDetails);
    }

    @MessageMapping("chat-rooms.{chatRoomId}.logs")
    @SendToUser("/queue/chat-rooms.logs")
    public List<ChatMessageRes> loadChatLogsAndMarkAsRead(@DestinationVariable Long chatRoomId, StompHeaderAccessor headerAccessor) {
        CustomUserDetails userDetails = Optional.ofNullable((CustomUserDetails) headerAccessor.getSessionAttributes().get("User"))
                .orElseThrow(() -> new IllegalStateException("User session not found"));
        log.info("[ChatController - loadChatLogsAndMarkAsRead] Called - chatRoomId: {}, user: {}", chatRoomId, userDetails.getUsername());
        return chatQueryService.getChatLogsAndMarkAsRead(chatRoomId, userDetails);
    }

    @MessageMapping("chat-rooms.{chatRoomId}.read")
    public void markAsRead(@DestinationVariable Long chatRoomId, @Payload String messageId, StompHeaderAccessor accessor) {
        CustomUserDetails userDetails = Optional.ofNullable((CustomUserDetails) accessor.getSessionAttributes().get("User"))
                .orElseThrow(() -> new IllegalStateException("User session not found"));
        log.info("[ChatController - markAsRead] Called - chatRoomId: {}, messageId: {}, user: {}", chatRoomId, messageId, userDetails.getUsername());
        chatCommandService.markMessageAsRead(chatRoomId, messageId, userDetails.getUsername());
    }
}
