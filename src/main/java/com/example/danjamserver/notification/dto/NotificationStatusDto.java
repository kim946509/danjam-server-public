package com.example.danjamserver.notification.dto;

import com.example.danjamserver.notification.enums.NotificationStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class NotificationStatusDto {
  @NotNull
  @NotEmpty(message = "알림의 ID 값은 필수입니다.")
  private List<Long> ids;

  @NotNull(message = "알림의 상태 값은 필수입니다.")
  private NotificationStatus status;
}
