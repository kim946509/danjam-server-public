package com.example.danjamserver.notice.service;

import com.example.danjamserver.notice.dto.FcmTokenDto;
import com.example.danjamserver.notice.dto.FcmTokenSendDto;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.RestResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface FcmRestTokenService {
    RestResponse<Object> sendMessageTo(FcmTokenSendDto fcmTokenSendDto) throws IOException;
    @Transactional
    RestResponse<Object> saveFcmToken(CustomUserDetails customUserDetails, FcmTokenDto fcmTokenDto);
}
