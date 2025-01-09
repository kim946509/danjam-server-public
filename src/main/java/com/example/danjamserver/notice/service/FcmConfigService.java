package com.example.danjamserver.notice.service;

import com.example.danjamserver.notice.dto.FcmTokenSendDto;
import com.example.danjamserver.notice.dto.FcmTopicSendDto;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.ApnsConfig;
import org.springframework.stereotype.Service;

@Service
public interface FcmConfigService {

    AndroidConfig tokenAndroidConfig(FcmTokenSendDto request);
    AndroidConfig topicAndroidConfig(FcmTopicSendDto request);

    ApnsConfig tokenApnsConfig(FcmTokenSendDto request);
    ApnsConfig topicApnsConfig(FcmTopicSendDto request);

    String getClientOs(String userAgent);
    void validateFcmTopicSendDto(FcmTopicSendDto dto);

    void validateFcmTokenSendDto(FcmTokenSendDto dto);
}
