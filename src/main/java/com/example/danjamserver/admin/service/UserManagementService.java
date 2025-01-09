package com.example.danjamserver.admin.service;


import com.example.danjamserver.admin.domain.RejectionReason;
import com.example.danjamserver.admin.dto.UserAuthenticationRequest;
import com.example.danjamserver.admin.repository.RejectionRepository;
import com.example.danjamserver.common.annotation.MethodDescription;
import com.example.danjamserver.notice.dto.FcmTokenSendDto;
import com.example.danjamserver.notice.service.FcmConfigService;
import com.example.danjamserver.notice.service.FcmTokenService;
import com.example.danjamserver.notification.dto.NotificationMessage;
import com.example.danjamserver.notification.enums.NotificationStatus;
import com.example.danjamserver.notification.enums.NotificationType;
import com.example.danjamserver.notification.rabbitmq.NotificationProducer;
import com.example.danjamserver.springSecurity.role.Role;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import com.example.danjamserver.util.exception.ResultCode;
import com.example.danjamserver.util.response.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagementService {

    private final UserRepository userRepository;
    private final RejectionRepository rejectionRepository;
    private final FcmTokenService fcmService;
    private final FcmConfigService fcmConfigService;
    private final NotificationProducer notificationProducer;

    /**
     * 사용자를 승인된 상태로 승격합니다.
     *
     * @param username 승격할 사용자의 사용자명
     * @return 권한 업그레이드 결과를 포함한 응답
     */
    @Transactional
    public RestResponse<String> promoteUserToAuthorizedStatus(HttpServletRequest request, String username)
            throws IOException {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            user.setRole(Role.AUTH_USER);
            userRepository.save(user);

            //알림 전송
            sendNotification(username, "승인 허용", "회원가입이 승인되었습니다.");

            return RestResponse.success("성공적으로 권한을 업그레이드했습니다.");
        } else {
            return RestResponse.error(ResultCode.CAN_NOT_FIND_USER_PROFILE);
        }
    }

    /**
     * 사용자의 인증 요청을 거부합니다.
     *
     * @param authRequest 사용자 인증 요청 정보
     * @return 거부 사유 저장 결과를 포함한 응답
     */
    @Transactional
    public RestResponse<String> denyUserAuthorization(HttpServletRequest request, UserAuthenticationRequest authRequest)
            throws IOException {
        String username = authRequest.getUsername();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return RestResponse.error(ResultCode.CAN_NOT_FIND_USER_PROFILE);
        }
        RejectionReason reason = RejectionReason.builder()
                .user(user)
                .reason(authRequest.getDescription())
                .build();
        rejectionRepository.save(reason);

        //알림 전송
        sendNotification(username, "승인 거부", "회원가입 승인 거부되었습니다. 거부사유: " + authRequest.getDescription());

        return RestResponse.success("거부 사유가 DB에 저장되었습니다.");
    }

    /**
     * 특정 기준에 따라 사용자를 검색합니다.
     *
     * @param query 검색 기준 (stranger, authuser, authfaileduser)
     * @return 검색된 사용자 목록을 포함한 응답
     */
    public RestResponse<List<User>> retrieveUsersBasedOnCriteria(String query) {
        List<User> users = switch (query) {
            case "stranger" -> userRepository.findUsersByRole(Role.STRANGER);
            case "authuser" -> userRepository.findUsersByRole(Role.AUTH_USER);
            case "authfaileduser" -> rejectionRepository.findUsersWithRejection();
            default -> userRepository.findAll();
        };
        return RestResponse.success(users);
    }

    /**
     * FCM Message Push
     *
     * @param userId
     * @param title, message
     */
    private void sendFcmNotification(HttpServletRequest request, Long userId, String title, String message) {
        String fcmToken = fcmService.getFcmToken(userId);
        String agent = request.getHeader("USER-AGENT");
        String os = fcmConfigService.getClientOs(agent);

        if (fcmToken != null) {
            FcmTokenSendDto fcmTokenSendDto = FcmTokenSendDto.builder()
                    .token(fcmToken)
                    .title(title)
                    .body(message)
                    .platform(os)
                    .build();
            fcmService.sendMessage(fcmTokenSendDto);
        } else {
            throw new InvalidTokenUser();
        }
    }

    @MethodDescription(description = "메시지를 RabbitMQ Queue 에 발행합니다.")
    private void sendNotification(String toName, String title, String content) {
        String routingKey = "notification.queue.notice";

        NotificationMessage message = NotificationMessage.builder()
                .receiverNames(List.of(toName))
                .type(NotificationType.ADMIN)
                .status(NotificationStatus.PENDING)
                .title(title)
                .content(content)
                .fromName("ADMIN")
                .url("")
                .build();
        try {
            notificationProducer.sendNotification(routingKey, message);
        } catch (Exception e) {
            log.error("Failed to send message to RabbitMQ", e);
        }
    }
}
