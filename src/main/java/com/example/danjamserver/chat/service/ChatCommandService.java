package com.example.danjamserver.chat.service;

import com.example.danjamserver.chat.domain.Chat;
import com.example.danjamserver.chat.domain.ChatType;
import com.example.danjamserver.chat.dto.requests.ChatCreateReq;
import com.example.danjamserver.chat.dto.responses.ChatMessageReadAckRes;
import com.example.danjamserver.chat.dto.responses.ChatMessageRes;
import com.example.danjamserver.chat.repository.ChatRepository;
import com.example.danjamserver.chatroom.domain.ChatRoom;
import com.example.danjamserver.chatroom.repository.ChatRoomRepository;
import com.example.danjamserver.chatroom.repository.ChatRoomUserRepository;
import com.example.danjamserver.common.annotation.MethodDescription;
import com.example.danjamserver.mate.domain.MateProfile;
import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.mate.repository.MateProfileRepository;
import com.example.danjamserver.notification.dto.NotificationMessage;
import com.example.danjamserver.notification.enums.NotificationStatus;
import com.example.danjamserver.notification.enums.NotificationType;
import com.example.danjamserver.notification.rabbitmq.NotificationProducer;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.BlockRepository;
import com.example.danjamserver.util.exception.ChatRoomNotFoundException;
import com.example.danjamserver.util.exception.ForbiddenAccessException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatCommandService {

    private static final Logger logger = LoggerFactory.getLogger(ChatCommandService.class);

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final BlockRepository blockRepository;
    private final MateProfileRepository mateProfileRepository;
    private final NotificationProducer notificationProducer;

    @Value("${rabbitmq.exchangeName}")
    private String exchangeName;

    // 메시지 전송
    public void sendMessage(Long chatRoomId, ChatCreateReq chatCreateReq, CustomUserDetails userDetails) {
        logger.info("[ChatCommandService] sendMessage called with chatRoomId: {}, user: {}, message: {}", chatRoomId,
                userDetails.getUsername(), chatCreateReq);

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> {
                    logger.error("[ChatCommandService] ChatRoomNotFoundException for chatRoomId: {}", chatRoomId);
                    return new ChatRoomNotFoundException();
                });

        if (!isUserInChatRoom(chatRoomId, userDetails.getId())) {
            logger.warn("[ChatCommandService] ForbiddenAccessException for user: {} in chatRoomId: {}",
                    userDetails.getUsername(), chatRoomId);
            throw new ForbiddenAccessException();
        }

        String message;
        MateType mateType = chatRoom.getChatRoomMateType();

        if (chatCreateReq.getChatType().equals(ChatType.PROFILE)) {
            Set<Long> blockedUserIds = blockRepository.findBlockedUsersByBlocker(userDetails.getUser())
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());

            Optional<MateProfile> profileOptional = mateProfileRepository.findByUserIdAndMateType(
                    userDetails.getId(), mateType
            ).filter(profile -> !blockedUserIds.contains(profile.getUser().getId()));

            message = profileOptional
                    .map(MateProfile::toString)
                    .orElse("Profile not found for this user and mate type.");
            logger.info("[ChatCommandService] PROFILE message prepared for user: {}", userDetails.getUsername());
        } else {
            message = chatCreateReq.getMessage();
            logger.info("[ChatCommandService] TEXT message prepared: {}", message);
        }

        List<String> unreadMembers = chatRoomUserRepository.findAllUsersByChatRoomId(chatRoomId)
                .stream()
                .filter(member -> !member.equals(userDetails.getUsername()))
                .collect(Collectors.toList());

        Chat chat = chatRepository.save(Chat.builder()
                .message(message)
                .sender(userDetails.getUsername())
                .chatRoomId(chatRoomId)
                .chatType(chatCreateReq.getChatType())
                .unreadMembers(unreadMembers)
                .build());
        logger.info("[ChatCommandService] Message saved with messageId: {}, unreadMembers count: {}", chat.getId(),
                unreadMembers.size());

        String routingKey = "chat-rooms." + chatRoomId;
        ChatMessageRes chatMessageRes = ChatMessageRes.create(chat);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, chatMessageRes);
        logger.info("[ChatCommandService] Message sent to RabbitMQ with routingKey: {}", routingKey);

        messagingTemplate.convertAndSend("/topic/chat-rooms." + chatRoomId, chatMessageRes);
        logger.info("[ChatCommandService] Message sent to STOMP topic: /topic/chat-rooms.{}", chatRoomId);

        // 채팅 메시지 알림 전송
        String chatRoomTitle = chatRoomRepository.findByTitle(chatRoomId);
        sendNotification(routingKey, unreadMembers, message, userDetails.getUsername(), chatRoomId, chatRoomTitle);
    }

    // 클라이언트로부터 메시지 읽음 처리 (Ack 받기)
    public void markMessageAsRead(Long chatRoomId, String messageId, String username) {
        logger.info("[ChatCommandService] markMessageAsRead called with chatRoomId: {}, messageId: {}, user: {}",
                chatRoomId, messageId, username);

        Chat chat = chatRepository.findById(messageId)
                .orElseThrow(() -> {
                    logger.error("[ChatCommandService] Message not found for messageId: {}", messageId);
                    return new IllegalArgumentException("Message not found");
                });

        if (chat.getUnreadMembers().contains(username)) {
            chat.getUnreadMembers().remove(username);
            chatRepository.save(chat);
            int unreadCount = chat.getUnreadMembers().size();
            logger.info("[ChatCommandService] User {} marked message as read. Remaining unread count: {}", username,
                    unreadCount);

            ChatMessageReadAckRes readAck = new ChatMessageReadAckRes(messageId, unreadCount);
            messagingTemplate.convertAndSend("/topic/chat-rooms." + chatRoomId + ".ack", readAck);
            logger.info("[ChatCommandService] Read acknowledgement sent to topic: /topic/chat-rooms.{}.ack",
                    chatRoomId);
        } else {
            logger.info("[ChatCommandService] User {} had already read the message with messageId: {}", username,
                    messageId);
        }
    }

    private boolean isUserInChatRoom(Long chatRoomId, Long userId) {
        boolean exists = chatRoomUserRepository.existsByChatRoomIdAndUserId(chatRoomId, userId);
        logger.info("[ChatCommandService] isUserInChatRoom check for chatRoomId: {}, userId: {} - Result: {}",
                chatRoomId, userId, exists);
        return exists;
    }

    @MethodDescription(description = "메시지를 RabbitMQ Queue 에 발행합니다.")
    private void sendNotification(String routingKey, List<String> unreadMember, String content, String sender,
                                  Long chatRoomId, String chatRoomTitle) {
        routingKey = "notification.queue." + routingKey;

        NotificationMessage message = NotificationMessage.builder()
                .receiverNames(Optional.ofNullable(unreadMember).orElse(List.of()))
                .type(NotificationType.CHATROOM)
                .status(NotificationStatus.PENDING)
                .title(chatRoomTitle + "에서 " + sender + "님이 보낸 알림이 도착했습니다.")
                .content(content)
                .fromName(sender)
                .url("https://mate.danjam.site/api/chat-rooms/" + chatRoomId)
                .build();
        try {
            notificationProducer.sendNotification(routingKey, message);
        } catch (Exception e) {
            logger.error("Failed to send message to RabbitMQ", e);
        }
    }
}
