package com.example.danjamserver.common.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheTestService {

    // 이 메서드가 Redis 캐시에서 데이터를 가져오게 함
    @Cacheable(value = "simpleCache", key = "#id")
    public String getDataFromCache(Long id) {
        // 첫 번째 호출에서만 이 메서드가 실행됨
        System.out.println("Fetching data from database for ID: " + id);
        return "Data for ID: " + id;
    }
}
