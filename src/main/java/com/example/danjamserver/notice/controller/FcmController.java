package com.example.danjamserver.notice.controller;


import com.example.danjamserver.notice.dto.FcmTokenDto;
import com.example.danjamserver.notice.dto.FcmTokenSendDto;
import com.example.danjamserver.notice.dto.FcmTopicSendDto;
import com.example.danjamserver.notice.service.FcmConfigService;
import com.example.danjamserver.notice.service.FcmTokenService;
import com.example.danjamserver.notice.service.FcmTopicService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseMessage;
import com.example.danjamserver.util.response.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class FcmController {

    private final FcmTokenService fcmTokenService;
    private final FcmTopicService fcmTopicService;
    private final FcmConfigService fcmConfigService;

    /**
     * 1 대 1 채팅 전송
     *
     * @param fcmTokenSendDto
     * @return
     */
    @PostMapping("/send")
    public ResponseEntity<RestResponse<Object>> pushMessage(HttpServletRequest request,
                                                            @RequestBody @Validated FcmTokenSendDto fcmTokenSendDto
    ) {
        String agent = request.getHeader("USER-AGENT");
        fcmTokenSendDto.setPlatform(fcmConfigService.getClientOs(agent));


        log.debug("[+] 푸시 메시지를 전송 합니다.");

        RestResponse<Object> response = fcmTokenService.sendMessage(fcmTokenSendDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * FCM TOKEN 저장
     *
     * @param customUserDetails
     * @param fcmTokenDto
     * @return
     */
    @PostMapping("/token")
    public ResponseEntity<RestResponse<Object>> saveFcmToken(
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody @Valid FcmTokenDto fcmTokenDto) {
        RestResponse<Object> response = fcmTokenService.saveFcmToken(customUserDetails, fcmTokenDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * fcm topic 채팅방 구독 알림 처리
     *
     * @param customUserDetails
     * @param chatRoomId
     * @return
     */
    @PostMapping("/topic/subscribe/{chatRoomId}")
    public ResponseEntity<ApiResponseMessage> subscribeTopic(
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("chatRoomId") Long chatRoomId) {
        fcmTopicService.subscribeTopic(customUserDetails, chatRoomId);

        return ResponseEntity.ok().body(ApiResponseMessage.of("채팅방 알림 구독이 완료 되었습니다."));
    }

    /**
     * fcm topic 채팅방 구독 알림 해제 처리
     *
     * @param customUserDetails
     * @param chatRoomId
     * @return
     */
    @PostMapping("/topic/unSubscribe/{chatRoomId}")
    public ResponseEntity<ApiResponseMessage> unSubscribeTopic(
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("chatRoomId") Long chatRoomId) {
        fcmTopicService.unSubscribeTopic(customUserDetails, chatRoomId);

        return ResponseEntity.ok().body(ApiResponseMessage.of("채팅방 알림 구독이 해제 되었습니다."));
    }

    @PostMapping("/topic/send")
    public ResponseEntity<RestResponse<Object>> pushMessage( HttpServletRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody FcmTopicSendDto sendDto) {
        String agent = request.getHeader("USER-AGENT");
        sendDto.setPlatform(fcmConfigService.getClientOs(agent));

        RestResponse<Object> response = fcmTopicService.sendChatRoomMessage(customUserDetails, sendDto);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
