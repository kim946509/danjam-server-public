package com.example.danjamserver.notification.repository.impl;

import com.example.danjamserver.notification.repository.EmitterRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Configuration
public class EmitterRepositoryImpl implements EmitterRepository {

  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

  @Override
  public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
    emitters.put(emitterId, sseEmitter);
    return sseEmitter;
  }

  @Override
  public void saveEventCache(String emitterId, Object event) {
    eventCache.put(emitterId, event);
  }

  @Override
  public Map<String, SseEmitter> findAllEmitterStartWithById(String userId) {
    return emitters.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(userId))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public Map<String, SseEmitter> getAllEmitters() {
    return emitters;
  }


  @Override
  public void deleteById(String id) {
    emitters.remove(id);
  }

  @Override
  public void deleteAllEmitterStartWithId(String userId) {
    emitters.forEach(
            (key, emitter) -> {
              if (key.startsWith(userId)) {
                emitters.remove(key);
              }
            }
    );
  }

  @Override
  public void deleteAllEventCacheStartWithId(String userId) {
    eventCache.forEach(
            (key, emitter) -> {
              if (key.startsWith(userId)) {
                eventCache.remove(key);
              }
            }
    );
  }
}
