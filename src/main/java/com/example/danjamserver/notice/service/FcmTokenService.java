package com.example.danjamserver.notice.service;

import com.example.danjamserver.notice.dto.FcmTokenSendDto;
import com.example.danjamserver.notice.dto.FcmTokenDto;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.RestResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface FcmTokenService {
    @Transactional
    RestResponse<Object> saveFcmToken(CustomUserDetails customUserDetails, FcmTokenDto fcmTokenDto);

    RestResponse<Object> sendMessage(FcmTokenSendDto tokenSendDto);

    String getFcmToken(Long userId);
}
