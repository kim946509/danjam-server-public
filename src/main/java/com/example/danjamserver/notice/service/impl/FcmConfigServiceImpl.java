package com.example.danjamserver.notice.service.impl;

import com.example.danjamserver.notice.dto.FcmTokenSendDto;
import com.example.danjamserver.notice.dto.FcmTopicSendDto;
import com.example.danjamserver.notice.service.FcmConfigService;
import com.example.danjamserver.util.exception.CustomValidationException;
import com.example.danjamserver.util.exception.ResultCode;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmConfigServiceImpl implements FcmConfigService {


    /**
     * Android 세팅
     * @param request
     * @return
     */
    @Override
    public AndroidConfig tokenAndroidConfig(FcmTokenSendDto request) {
        return AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build())
                .build();
    }

    /**
     * Android 세팅
     * @param request
     * @return
     */
    @Override
    public AndroidConfig topicAndroidConfig(FcmTopicSendDto request) {
        return AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build())
                .build();
    }


    /**
     * Ios 세팅
     * @param request
     * @return
     */
    @Override
    public ApnsConfig tokenApnsConfig(FcmTokenSendDto request) {
        return ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setAlert(
                                ApsAlert.builder()
                                        .setTitle(request.getTitle())
                                        .setBody(request.getBody())
                                        //.setLaunchImage(request.getImgUrl()) // 이미지 URL 설정
                                        .build()
                        )
                        .setSound("default")
                        .build())
                .build();
    }


    /**
     * Ios 세팅
     * @param request
     * @return
     */
    @Override
    public ApnsConfig topicApnsConfig(FcmTopicSendDto request) {
        return ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setAlert(
                                ApsAlert.builder()
                                        .setTitle(request.getTitle())
                                        .setBody(request.getBody())
                                        //.setLaunchImage(request.getImgUrl()) // 이미지 URL 설정
                                        .build()
                        )
                        .setSound("default")
                        .build())
                .build();
    }

    /**
     * os 분기처리
     * @param userAgent
     * @return
     */
    @Override
    public String getClientOs(String userAgent) {
        String os = "";
        userAgent = Optional.ofNullable(userAgent)
                .map(String::toLowerCase)
                .orElse("unknown");

        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("android")) {
            os = "android";
        } else if (userAgent.contains("iphone")) {
            os = "ios";
        } else if (userAgent.contains("ipad")) {
            os = "ios";
        } else if (userAgent.contains("ios")) {
            os = "ios";
        } else {
            os = "unknown";
        }
        return os;
    }

    /**
     * FCM 전송 요청 DTO의 유효성을 검증합니다.
     *
     * @param dto FcmTopicSendDto
     */
    @Override
    public void validateFcmTopicSendDto(FcmTopicSendDto dto) {
        if (dto.getTopic() == null || dto.getTopic().isBlank()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "FCM 토픽은 필수입니다.");
        }
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "제목은 필수입니다.");
        }
        if (dto.getBody() == null || dto.getBody().isBlank()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "내용은 필수입니다.");
        }
        if (dto.getPlatform().equalsIgnoreCase("unknown")) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "os가 존재하지 않습니다.");
        }
    }

    /**
     * FCM 전송 요청 DTO의 유효성을 검증합니다.
     *
     * @param dto FcmTokenSendDto
     */
    @Override
    public void validateFcmTokenSendDto(FcmTokenSendDto dto) {
        if (dto.getToken() == null || dto.getToken().isBlank()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "FCM 토큰은 필수입니다.");
        }
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "제목은 필수입니다.");
        }
        if (dto.getBody() == null || dto.getBody().isBlank()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "내용은 필수입니다.");
        }
        if (dto.getPlatform().equalsIgnoreCase("unknown")) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "os가 존재하지 않습니다.");
        }
    }
}
