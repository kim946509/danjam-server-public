package com.example.danjamserver.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.util.AntPathMatcher;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketConfig.class);
    private final RabbitProperties rabbitProperties;
    private final ChatPreHandler chatPreHandler;

    public WebsocketConfig(RabbitProperties rabbitProperties, ChatPreHandler chatPreHandler) {
        this.rabbitProperties = rabbitProperties;
        this.chatPreHandler = chatPreHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        logger.info("Configuring STOMP with host: {}, port: {}, username: {}, password: {}",
                rabbitProperties.getHost(), 61613,
                rabbitProperties.getUsername(), rabbitProperties.getPassword());

        registry.setPathMatcher(new AntPathMatcher("."));
        registry.setApplicationDestinationPrefixes("/pub");
        registry.setUserDestinationPrefix("/user");
        registry.enableStompBrokerRelay("/queue", "/topic")
                .setClientLogin(rabbitProperties.getUsername())
                .setClientPasscode(rabbitProperties.getPassword())
                .setSystemLogin(rabbitProperties.getUsername())
                .setSystemPasscode(rabbitProperties.getPassword())
                .setRelayHost(rabbitProperties.getHost())
                .setRelayPort(61613)
                .setVirtualHost("/");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(chatPreHandler);
    }
}
