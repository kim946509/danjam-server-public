package com.example.danjamserver.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbitmq")
@Getter @Setter
public class RabbitProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String queueName;
    private String exchangeName;
    private String routingKey;
}
