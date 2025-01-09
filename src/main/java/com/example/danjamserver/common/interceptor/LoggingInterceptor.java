package com.example.danjamserver.common.interceptor;

import com.example.danjamserver.common.interceptor.dto.RequestResponseWrappers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;


/**
 *  HTTP 요청 및 세부 정보를 로깅하기 위한 인터셉터 클래스
 */

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
            throws Exception {
        // 요청과 응답을 래핑할 때 ContentCachingRequestWrapper 및 ContentCachingResponseWrapper를 사용할 수 있는지 확인
        HttpServletRequest wrappedRequest = (request instanceof ContentCachingRequestWrapper)
                ? (ContentCachingRequestWrapper) request
                : new ContentCachingRequestWrapper(request);

        HttpServletResponse wrappedResponse = (response instanceof ContentCachingResponseWrapper)
                ? (ContentCachingResponseWrapper) response
                : new ContentCachingResponseWrapper(response);

        // 로그 메시지를 생성
        RequestResponseWrappers wrappers = new RequestResponseWrappers(wrappedRequest, wrappedResponse);

        // 응답 상태에 따라 로그 수준을 조정하여 기록
        if (wrappedResponse.getStatus() >= 500) {
            log.error(wrappers.requestMessageWithResponseStatus());
        } else if (wrappedResponse.getStatus() >= 400) {
            log.warn(wrappers.requestMessageWithResponseStatus());
        } else {
            log.info(wrappers.requestAndResponseMessage());
        }
    }
}
