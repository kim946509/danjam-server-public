package com.example.danjamserver.common.interceptor;


import com.example.danjamserver.common.annotation.MethodDescription;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class ServletWrappingFilter implements Filter {

    @MethodDescription(description = "HttpServletRequest(Response) 를 ContentCachingRequestWrapper(Response) 로 래핑하여 요청, 응답 본문을 캐싱할 수 있게 하는 필터")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (shouldExcludeFromFilter(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = (httpRequest instanceof ContentCachingRequestWrapper)
                ? (ContentCachingRequestWrapper) httpRequest
                : new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper responseWrapper = (httpResponse instanceof ContentCachingResponseWrapper)
                ? (ContentCachingResponseWrapper) httpResponse
                : new ContentCachingResponseWrapper(httpResponse);

        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            responseWrapper.copyBodyToResponse();
        }
    }

    @MethodDescription(description = "특정 경로 (/api/notice/subscribe) 요청을 필터에서 제외합니다.")
    private boolean shouldExcludeFromFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return "/api/notice/subscribe".equals(requestURI);
    }
}
