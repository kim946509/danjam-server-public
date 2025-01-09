package com.example.danjamserver.notice.service;

import com.example.danjamserver.notice.dto.FcmTopicSendDto;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.RestResponse;
import org.springframework.transaction.annotation.Transactional;

public interface FcmTopicService {
    RestResponse<Object> sendChatRoomMessage(CustomUserDetails customUserDetails, FcmTopicSendDto fcmTopicSendDto);
    void subscribeTopic(CustomUserDetails customUserDetails,Long chatRoomId);
    void unSubscribeTopic(CustomUserDetails customUserDetails, Long chatRoomId);
    @Transactional
    void deleteTopic(Long chatRoomId);
}
