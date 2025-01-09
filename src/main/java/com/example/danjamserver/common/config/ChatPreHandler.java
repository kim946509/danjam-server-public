package com.example.danjamserver.common.config;

import com.example.danjamserver.springSecurity.jwt.JWTUtil;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ChatPreHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getCommand() == StompCommand.CONNECT ||
                accessor.getCommand() == StompCommand.SEND) {
            String accessToken = accessor.getFirstNativeHeader("access");
            if (accessToken != null) {
                System.out.println("checked user");
                String username = jwtUtil.getUsername(accessToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Objects.requireNonNull(accessor.getSessionAttributes()).put("User", userDetails);
                System.out.println("User added to session: " + username);
            }
        }
        return message;
    }
}
