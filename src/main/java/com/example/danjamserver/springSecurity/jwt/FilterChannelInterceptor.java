package com.example.danjamserver.springSecurity.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.Message;

@Slf4j
@RequiredArgsConstructor
public class FilterChannelInterceptor implements ChannelInterceptor {
    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        log.info("Stomp Interceptor In");
        if (headerAccessor == null) {
            throw new IllegalStateException("HeaderAccessor is null");
        }
        if (headerAccessor.getCommand() == StompCommand.CONNECT || headerAccessor.getCommand() == StompCommand.SUBSCRIBE || headerAccessor.getCommand() == StompCommand.SEND) {
            String accessToken = headerAccessor.getFirstNativeHeader("access");
            if (accessToken != null) {
                String username = jwtUtil.getUsername(accessToken);
                headerAccessor.addNativeHeader("User", String.valueOf(username));
            }
        }
        return MessageBuilder.createMessage(message.getPayload(),
                headerAccessor.getMessageHeaders());
    }
}
