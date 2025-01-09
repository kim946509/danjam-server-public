package com.example.danjamserver.util.exception;

import lombok.Getter;

@Getter
public class SseEmitterException extends BaseException{
  public SseEmitterException() {
    super(ResultCode.SSE_EMITTER_ERROR);
  }

  public SseEmitterException(String detailMessage) {
    super(ResultCode.SSE_EMITTER_ERROR, detailMessage);
  }
}
