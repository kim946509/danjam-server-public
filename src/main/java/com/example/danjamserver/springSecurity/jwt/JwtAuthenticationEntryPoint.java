package com.example.danjamserver.springSecurity.jwt;

import com.example.danjamserver.util.exception.ResultCode;
import com.example.danjamserver.util.response.ApiResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * 로그인 되지 않은 사용자 혹은 유효하지 않은 토큰 사용자의 예외 처리를 담당
 * JWTFilter 클래스에서 response에 Attribute로 ResultCode를 담아서 해당 예외를 처리
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException,  IOException {
        ResultCode resultCode = (ResultCode)request.getAttribute("exception");
        if(resultCode.equals(ResultCode.REQUIRED_LOGIN)){
            exceptionHandler(response, resultCode);
        }
        if(resultCode.equals(ResultCode.INVALID_TOKEN)){
            exceptionHandler(response, resultCode);
        }
        if(resultCode.equals(ResultCode.EXPIRED_TOKEN)){
            exceptionHandler(response, resultCode);
        }
        if(resultCode.equals(ResultCode.INTERNAL_SERVER_ERROR)){
            exceptionHandler(response, resultCode);
        }
    }

    public void exceptionHandler(HttpServletResponse response, ResultCode resultCode) {
        ApiResponseError responseError = ApiResponseError.of(resultCode);
        response.setStatus(resultCode.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(objectMapper.writeValueAsString(responseError));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}