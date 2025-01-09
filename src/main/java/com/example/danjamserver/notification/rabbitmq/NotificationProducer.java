package com.example.danjamserver.notification.rabbitmq;

import com.example.danjamserver.common.annotation.MethodDescription;
import com.example.danjamserver.notification.dto.NotificationMessage;
import com.example.danjamserver.util.exception.RabbitMqException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NotificationProducer {
  private String exchangeName = "notification.exchange";

  private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class.getName());

  private final RabbitTemplate rabbitTemplate;

  @MethodDescription(description = "RabbitMQ Producer 를 통해 메시지를 Queue 에 발행합니다.")
  public void sendNotification(String routingKey, NotificationMessage message) {
    try {
      rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
      logger.info("Message sent to exchange " + exchangeName + " with routingKey " + routingKey);
    } catch (Exception e) {
      logger.error("Failed sent to exchange: {}, routingKey: {}, message: {}", exchangeName, routingKey, message);
      throw new RabbitMqException("RabbitMq 메시지 발행에 실패했습니다.");
    }
  }
}
