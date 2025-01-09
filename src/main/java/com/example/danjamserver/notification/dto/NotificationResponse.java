package com.example.danjamserver.notification.dto;


import com.example.danjamserver.notification.domain.Notification;
import com.example.danjamserver.notification.enums.NotificationStatus;
import com.example.danjamserver.notification.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {
  private Long id; // 알림 고유 ID
  private Long receiverId; // 수신자
  private NotificationType type; // 알림 타입
  private NotificationStatus status; // 알림 상태
  private String title; // 제목
  private String content; // 알림 내용
  private String fromName; // 발신인 이름
  private String url; // 관련 URL(link 이동)

  public static NotificationResponse from(Notification notification) {
    return NotificationResponse.builder()
            .id(notification.getId())
            .receiverId(notification.getReceiver().getId())
            .type(notification.getType())
            .status(notification.getStatus())
            .title(notification.getTitle())
            .content(notification.getContent())
            .fromName(notification.getFromName())
            .url(notification.getUrl())
            .build();
  }
}
