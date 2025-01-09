package com.example.danjamserver.springSecurity.jwt;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.springSecurity.service.CustomUserDetailsService;
import com.example.danjamserver.util.exception.ResultCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JWTFilter(JWTUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    //JWT 토큰 검증
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        //토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            request.setAttribute("exception", ResultCode.REQUIRED_LOGIN); //로그인이 필요한 상태. EntryPoint에서 처리
            filterChain.doFilter(request, response);
            return;
        }

        //토큰이 유효한지 확인
        switch (jwtUtil.isValidToken(accessToken)) {
            case 0: //유효한 토큰
                break;
            case 1: //만료된 토큰
                request.setAttribute("exception", ResultCode.EXPIRED_TOKEN); //만료된 토큰. EntryPoint에서 처리
                filterChain.doFilter(request, response);
                return;
            case 2: //유효하지 않은 토큰
                request.setAttribute("exception", ResultCode.INVALID_TOKEN); //유효하지 않은 토큰. EntryPoint에서 처리
                filterChain.doFilter(request, response);
                return;
        }

        //토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.getUsername(accessToken);
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}