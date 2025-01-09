package com.example.danjamserver.notification.domain;


import com.example.danjamserver.notification.enums.NotificationStatus;
import com.example.danjamserver.notification.enums.NotificationType;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "noti")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId")
  @NotNull
  private User receiver;

  @Enumerated(value = EnumType.STRING)
  @NotNull
  private NotificationType type;

  @Enumerated(value = EnumType.STRING)
  @NotNull
  private NotificationStatus status;

  @NotNull
  private String title; // 제목
  private String content; // 메시지 내용
  private String fromName; // 발신인
  private String url; // 관련 url(link)
}
