package com.example.danjamserver.util.exception;

import lombok.Getter;

@Getter
public class RabbitMqException extends BaseException{
    public RabbitMqException() {
      super(ResultCode.RabbitMq_ERROR);
    }

    public RabbitMqException(String detailMessage) {
      super(ResultCode.RabbitMq_ERROR, detailMessage);
    }

}
