package com.example.danjamserver.common.interceptor.dto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * HTTP 요청과 응답을 래핑하는 클래스
 */
public class RequestResponseWrappers {
    // 요청과 응답의 내용을 캐시하는 래퍼 객체
    private final ContentCachingRequestWrapper requestWrapper;
    private final ContentCachingResponseWrapper responseWrapper;

    // 생성자로 초기화
    public RequestResponseWrappers(HttpServletRequest request, HttpServletResponse response) {
        requestWrapper = (ContentCachingRequestWrapper) request;
        responseWrapper = (ContentCachingResponseWrapper) response;
    }

    /**
     * 요청과 응답의 세부 정보를 포함하는 로그 메시지를 생성합니다. 이 메시지에는 HTTP 메서드, URI, 쿼리 스트링, 헤더, 요청 본문, 응답 상태 및 응답 본문이 포함됩니다.
     *
     * @return 요청 및 응답의 세부 정보를 포함하는 포맷된 문자열
     */
    public String requestAndResponseMessage() {
        return requestMessageWithResponseStatus()
                +
                String.format(", Body: %s", new String(responseWrapper.getContentAsByteArray()));
    }

    /**
     * 요청의 세부 정보와 응답 상태를 포함하는 로그 메시지를 생성합니다. 이 메시지에는 HTTP 메서드, URI, 쿼리 스트링, 헤더, 요청 본문 및 응답 상태가 포함됩니다.
     *
     * @return 요청 세부 정보와 응답 상태를 포함하는 포맷된 문자열
     */
    public String requestMessageWithResponseStatus() {
        return String.format("[Request] %s %s, Query: %s, Headers: %s, Body: %s [Response] %s",
                requestWrapper.getMethod(),
                requestWrapper.getRequestURI(),
                requestWrapper.getQueryString(),
                requestWrapper.getHeader("access"),
                removeNewlines(new String(requestWrapper.getContentAsByteArray())),
                responseWrapper.getStatus()
        );
    }

    // 문자열에서 개행 문자와 불필요한 공백을 제거하는 메서드.
    private String removeNewlines(String body) {
        return body.replaceAll("\\n|\\s{2}", "");
    }
}
