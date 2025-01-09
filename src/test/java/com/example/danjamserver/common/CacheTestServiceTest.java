package com.example.danjamserver.common;

import com.example.danjamserver.common.service.CacheTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

// Redis가 정상적으로 동작하는지 Test
@SpringBootTest
public class CacheTestServiceTest {

    @Autowired
    private CacheTestService cacheTestService;

    @Test
    public void testCacheBehavior() {
        Long id = 1L;

        // 첫 번째 호출: 메서드가 실행되어 데이터를 생성하고 캐시에 저장
        String firstCall = cacheTestService.getDataFromCache(id);
        assertThat(firstCall).isEqualTo("Data for ID: " + id);

        // 두 번째 호출: 캐시된 데이터를 가져오므로 메서드가 실행되지 않음
        String secondCall = cacheTestService.getDataFromCache(id);
        assertThat(secondCall).isEqualTo(firstCall);

        // 두 번째 호출에서도 메서드가 실행되지 않았음을 확인하기 위해 로그 확인
        System.out.println("캐시에서 데이터를 반환했는지 확인");
    }
}
