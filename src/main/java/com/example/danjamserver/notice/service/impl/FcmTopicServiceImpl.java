package com.example.danjamserver.notice.service.impl;

import com.example.danjamserver.notice.domain.FcmTopic;
import com.example.danjamserver.notice.dto.FcmTopicSendDto;
import com.example.danjamserver.notice.repository.FcmTopicRepository;
import com.example.danjamserver.notice.repository.NoticeRepository;
import com.example.danjamserver.notice.service.FcmConfigService;
import com.example.danjamserver.notice.service.FcmTopicService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.FcmSubscriptionAccessException;
import com.example.danjamserver.util.exception.FcmTokenNotFoundException;
import com.example.danjamserver.util.exception.FcmTopicNotFoundException;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import com.example.danjamserver.util.exception.ResultCode;
import com.example.danjamserver.util.response.RestResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.TopicManagementResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class FcmTopicServiceImpl implements FcmTopicService {

    private final FcmTopicRepository fcmTopicRepository;
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final FcmConfigService fcmConfigService;

    /**
     * topic 구독 설정
     * @param customUserDetails
     * @param chatRoomId
     */
    public void subscribeTopic(CustomUserDetails customUserDetails, Long chatRoomId) {
        try {
            String userName = customUserDetails.getUsername();

            User user = userRepository.findByUsername(userName)
                    .orElseThrow(InvalidTokenUser::new);

            String token = Optional.ofNullable(noticeRepository.findTokenByUserId(user.getId()))
                    .orElseThrow(FcmTokenNotFoundException::new);

            FcmTopic topic = saveOrGetTopic(chatRoomId);
            user.getTopics().add(topic);
            userRepository.save(user);

            List<String> registrationTokens = Arrays.asList(token);


            TopicManagementResponse response = FirebaseMessaging
                    .getInstance().subscribeToTopic(registrationTokens, topic.getName());
            log.info(response.getSuccessCount() + "[+] 채팅방 알림 구독이 완료 되었습니다.");

        } catch (FirebaseMessagingException e) {
            throw new FcmSubscriptionAccessException();
        }
    }

    /**
     * topic 구독 해제
     * @param customUserDetails
     * @param chatRoomId
     */
    public void unSubscribeTopic(CustomUserDetails customUserDetails, Long chatRoomId) {
        try {
            String userName = customUserDetails.getUsername();

            User user = userRepository.findByUsername(userName)
                    .orElseThrow(InvalidTokenUser::new);

            String token = Optional.ofNullable(noticeRepository.findTokenByUserId(user.getId()))
                    .orElseThrow(FcmTokenNotFoundException::new);

            FcmTopic topic = saveOrGetTopic(chatRoomId);
            user.getTopics().remove(topic);
            userRepository.save(user);

            List<String> registrationTokens = Arrays.asList(token);

            TopicManagementResponse response = FirebaseMessaging
                    .getInstance().unsubscribeFromTopic(registrationTokens, topic.getName());
            log.info(response.getSuccessCount() + "[+] 채팅방 알림 구독이 취소 되었습니다.");

        } catch (FirebaseMessagingException e) {
            throw new FcmSubscriptionAccessException();
        }
    }

    /**
     * FCM 메세지 생성 및 전송 FCM SDK 방식
     * @param fcmTopicSendDto
     * @return 성공 시 CODE (0)
     */
    @Override
    public RestResponse<Object> sendChatRoomMessage(CustomUserDetails customUserDetails, FcmTopicSendDto fcmTopicSendDto) {
        try {
            String username = customUserDetails.getUsername();

            Long topicId = Optional.ofNullable(userRepository.findTopicIdsByUsername(username)).orElseThrow(FcmTopicNotFoundException::new);
            String topic = fcmTopicRepository.findTopicNameById(topicId).orElseThrow(FcmTopicNotFoundException::new);

            fcmConfigService.validateFcmTopicSendDto(fcmTopicSendDto);

            Message.Builder message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(fcmTopicSendDto.getTitle())
                            .setBody(username + ": " + fcmTopicSendDto.getBody())
                            .build())
                    .setTopic(topic);

            // ios, android config
            switch (fcmTopicSendDto.getPlatform().toLowerCase()) {
                case "android" -> message.setAndroidConfig(fcmConfigService.topicAndroidConfig(fcmTopicSendDto));
                case "ios" -> message.setApnsConfig(fcmConfigService.topicApnsConfig(fcmTopicSendDto));
            }

            send(message.build()); //메시지 전송
            return RestResponse.success("메시지 전송 요청 완료");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.error(ResultCode.INTERNAL_SERVER_ERROR,
                    "메시지 전송 실패: " + e.getMessage());
        }
    }

    /**
     * FCM 메시지 전송
     *
     * @param message
     */
    public void send(Message message) {
        String response = String.valueOf(FirebaseMessaging.getInstance().sendAsync(message));
        log.info("Successfully sent message: " + response);
    }

    /**
     * Topic 저장 및 가져오기
     * @param chatRoomId
     */
    @Transactional
    public FcmTopic saveOrGetTopic(Long chatRoomId) {
        String topicName = "chat_room_" + chatRoomId;

        try {
            Optional<FcmTopic> existingTopic = Optional.ofNullable(
                    fcmTopicRepository.findByName(topicName).orElseThrow(FcmTopicNotFoundException::new));

            if (existingTopic.isPresent()) {
                return existingTopic.get();
            }

            FcmTopic fcmTopic = FcmTopic.builder()
                    .name(topicName)
                    .build();

            return fcmTopicRepository.save(fcmTopic);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new InvalidTokenUser();
        }
    }
    /**
     * Topic 삭제
     *
     * @param chatRoomId
     */
    public void deleteTopic(Long chatRoomId) {
        String topicName = "chat_room_" + chatRoomId;

        try {
            Optional<FcmTopic> existingTopic = Optional.ofNullable(
                    fcmTopicRepository.findByName(topicName).orElseThrow(FcmTopicNotFoundException::new));

            if (existingTopic.isPresent()) {
                FcmTopic topic = existingTopic.get();
                fcmTopicRepository.delete(topic);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
