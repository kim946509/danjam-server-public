package com.example.danjamserver.notification.controller;


import com.example.danjamserver.notification.dto.NotificationResponse;
import com.example.danjamserver.notification.dto.NotificationStatusDto;
import com.example.danjamserver.notification.service.NotificationService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * SSE 관련 Controller
 * @author 유다인
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NotificationController {
  private final NotificationService notificationService;

  /**
   * SSE 연결 구독을 진행합니다.
   * @RequestHeader Last-Event-Id<String> lastEventId 클라이언트가 마지막으로 수신한 데이터의 ID 값입니다. (항상 들어오는 값이 아닙니다.)
   * @CustomUserDetails customUserDetails
   * @Author 유다인
   */
  @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestHeader(value = "Last-Event-Id", required = false, defaultValue = "") String lastEventId) {
    return notificationService.subscribe(customUserDetails, lastEventId);
  }

  /**
   * 알림을 읽음/삭제 처리합니다.
   * @CustomUserDetails customUserDetails
   * @NotificationStatusDto dto
   * @Author 유다인
   */
  @PatchMapping(value = "/status")
  public ResponseEntity<ApiResponseMessage> updateNotificationStatus(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody @Valid NotificationStatusDto dto) {
    return ResponseEntity.ok().body(notificationService.processNotificationStatus(customUserDetails, dto.getIds(), dto.getStatus()));
  }

  /**
   * 알림 내역을 조회합니다.
   * @param customUserDetails
   * @Author 유다인
   */
  @GetMapping
  public ResponseEntity<List<NotificationResponse>> readNotificationMessage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
    return ResponseEntity.ok().body(notificationService.readNotifications(customUserDetails));
  }
}
