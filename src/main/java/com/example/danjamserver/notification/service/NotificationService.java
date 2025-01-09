package com.example.danjamserver.notification.service;


import com.example.danjamserver.common.annotation.MethodDescription;
import com.example.danjamserver.notification.domain.Notification;
import com.example.danjamserver.notification.dto.NotificationMessage;
import com.example.danjamserver.notification.dto.NotificationResponse;
import com.example.danjamserver.notification.enums.NotificationStatus;
import com.example.danjamserver.notification.enums.NotificationType;
import com.example.danjamserver.notification.repository.EmitterRepository;
import com.example.danjamserver.notification.repository.NotificationRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.*;

import java.util.List;
import java.util.Optional;

import com.example.danjamserver.util.response.ApiResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
  private final EmitterRepository emitterRepository;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  // 연결 지속시간 (한시간)
  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 1000;

  @MethodDescription(description = "sse 구독을 진행합니다.")
  public SseEmitter subscribe(CustomUserDetails customUserDetails, String lastEventId) {
    Long userId = customUserDetails.getId();

    // emitterUserId Create
    String emitterId = makeTimeIncludeUserId(userId);

    // SseEmitter 객체를 만들고 반환, key: id, value: SseEmitter
    SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

    // 시간 초과, 비동기 요청이 되지 않으면 자동으로 삭제
    emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
    emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

    // 503 오류 방지를 위한 더미데이터 전송
    sendNotification(emitter, emitterId, "EventStream Created. [user = " + userId + " ]");

    if (hasLostData(lastEventId)) {
      sendLostData(lastEventId, userId, emitter);
    }

    return emitter;
  }

  @MethodDescription(description = "사용자의 ID와 현재 시간을 조합하여 고유한 emitter ID를 생성합니다.")
  private String makeTimeIncludeUserId(Long userId) {
    return userId + "_" + System.currentTimeMillis();
  }

  @MethodDescription(description = "SSE 연결을 통해 클라이언트에게 데이터를 전송합니다. 실패 시 Emitter를 삭제합니다.")
  private void sendNotification(SseEmitter emitter, String emitterId, Object data) {
    try {
      emitter.send(SseEmitter.event()
              .name("notification")
              .id(emitterId)
              .data(data)
      );
    } catch (IOException e) {
      emitterRepository.deleteById(emitterId);
      throw new SseEmitterException("메시지 전송을 실패했습니다.");
    }
  }

  @MethodDescription(description = "Last-Event-Id 가 존재하는 지 확인합니다.")
  private Boolean hasLostData(String lastEventId) {
    return !lastEventId.isEmpty();
  }

  @MethodDescription(description = "마지막으로 수신한 이벤트 ID 이후의 데이터를 검색하고, SSE 연결을 통해 클라이언트로 전송합니다.")
  private void sendLostData(String lastEventId, Long userId, SseEmitter emitter) {
    Map<String, SseEmitter> eventCaches = emitterRepository.findAllEmitterStartWithById(String.valueOf(userId));

    eventCaches.entrySet().stream()
            .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
            .forEach(entry -> sendNotification(emitter, entry.getKey(), entry.getValue()));
  }

  @MethodDescription(description = "특정 사용자들에게 알림을 전송합니다.")
  @Async
  public void send(List<String> toName, NotificationType type, NotificationStatus status, String title, String content, String fromName, String url) {
    List<User> receivers = toName.stream()
            .map(userRepository::findByUsername)
            .flatMap(Optional::stream)
            .toList();

    receivers.forEach(receiver -> {
      Notification noti = notificationRepository.save(createNotification(receiver, type, status, title, content, url, fromName));

      Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithById(String.valueOf(receiver.getId()));

      sseEmitters.forEach(
              (key, emitter) -> {
                emitterRepository.saveEventCache(key, noti);
                sendNotification(emitter, key, NotificationResponse.from(noti));
              }
      );
    });
  }

  @MethodDescription(description = "알림 객체를 생성합니다.")
  public Notification createNotification(User receiver, NotificationType type, NotificationStatus status, String title, String content, String url, String fromName) {
    return Notification.builder()
            .receiver(receiver)
            .type(type)
            .status(status)
            .title(title)
            .content(content)
            .url(url)
            .fromName(fromName)
            .build();
  }

  @MethodDescription(description = "RabbitMQ Consumer 를 통해서 메시지를 받습니다.")
  @RabbitListener(queues = "notification.queue")
  public void consumeNotificationMessage(NotificationMessage message) {
    try {
      log.info("Received message: {}", message);
      send(message.getReceiverNames(),
              message.getType(),
              message.getStatus(),
              message.getTitle(),
              message.getContent(),
              message.getFromName(),
              message.getUrl()
      );
    } catch (Exception e) {
      log.error("Error deserializing message from RabbitMQ: {}", e.getMessage());
      throw new RabbitMqException("RabbitMQ 메시지 역질렬화 중 에러가 발생했습니다.");
    }
  }

  @MethodDescription(description = "알림의 상태를 바꿉니다.")
  @Transactional
  public ApiResponseMessage processNotificationStatus(CustomUserDetails customUserDetails, List<Long> ids, NotificationStatus status) {
    Long userId = customUserDetails.getId();
    try {
      int updatedCount = notificationRepository.updateNotificationStatus(userId, ids, status);

      if (updatedCount == 0) {
        throw new InvalidResourceException(ResultCode.ALREADY_EXIST_NOTIFICATION);
      }
      return ApiResponseMessage.of("알림 상태가 정상적으로 업데이트 되었습니다.");
    } catch (Exception e) {
      log.error("Error updating notification status: {}", e.getMessage());
      throw new InvalidResourceException(ResultCode.ALREADY_EXIST_NOTIFICATION);
    }
  }

  @MethodDescription(description = "사용자가 삭제하지 않은 모든 알림을 조회합니다.")
  @Transactional(readOnly = true)
  public List<NotificationResponse> readNotifications(CustomUserDetails customUserDetails) {
    String username = customUserDetails.getUsername();

    User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("유저가 존재하지 않습니다."));

    List<Notification> notifications = notificationRepository.findByReceiverId(user.getId());
    return notifications.stream()
            .map(NotificationResponse::from)
            .collect(Collectors.toList());
  }
}