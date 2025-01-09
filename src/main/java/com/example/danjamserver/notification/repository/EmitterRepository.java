package com.example.danjamserver.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Repository
public interface EmitterRepository {
  SseEmitter save(String emitterId, SseEmitter sseEmitter);
  void saveEventCache(String emitterId, Object event);
  Map<String, SseEmitter> findAllEmitterStartWithById(String userId);
  public Map<String, SseEmitter> getAllEmitters();

  void deleteById(String id);
  void deleteAllEmitterStartWithId(String userId);
  void deleteAllEventCacheStartWithId(String userId);
}
