package com.example.danjamserver.springSecurity.jwt;


import com.example.danjamserver.springSecurity.domain.RefreshToken;
import com.example.danjamserver.springSecurity.dto.UserBasicInformation;
import com.example.danjamserver.springSecurity.repository.RefreshTokenRepository;
import com.example.danjamserver.springSecurity.service.UserBasicInformationService;
import com.example.danjamserver.user.dto.LoginDTO;
import com.example.danjamserver.util.exception.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserBasicInformationService userBasicInformationService;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                       RefreshTokenRepository refreshTokenRepository,
                       UserBasicInformationService userBasicInformationService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userBasicInformationService = userBasicInformationService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        LoginDTO loginDTO = new LoginDTO();

        try {
            ObjectMapper objectMapper = new ObjectMapper();//json을 객체로 변환하기 위한 objectMapper
            ServletInputStream inputStream = request.getInputStream();//요청에서 데이터를 추출하기 위한 inputStream
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);//inputStream을 문자열로 변환
            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);//json을 객체로 변환
        }
        //json에 없는 필드가 들어왔을 때 400 에러를 반환
        catch (UnrecognizedPropertyException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        //authenticationManager에게 username과 password를 검증하라고 요청
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password,
                null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) {

        //유저 정보
        String username = authentication.getName();
        //반복자를 이용하여 role을 획득
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L);  // 10분
        String refresh = jwtUtil.createJwt("refresh", username, role, 1800000L);    // 30분. 하루 : 86400000L

        //Refresh 토큰 저장
        addRefreshToken(username, refresh, 1800000L); // 30분

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

        //response body에 유저의 정보를 전달
        UserBasicInformation userBasicInformation = userBasicInformationService.getUserBasicInformation(username);

        // 유저 정보를 JSON으로 변환
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String userJson = objectMapper.writeValueAsString(userBasicInformation);
            // 응답 본문에 JSON 작성
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(userJson);
        } catch (IOException e) {
            request.setAttribute("exception", ResultCode.INTERNAL_SERVER_ERROR);
        }


    }


    // 로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {
        response.setStatus((401));
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(24*60*60); // 24시간
        cookie.setMaxAge(1800); // 30분
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshToken(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setRefresh(refresh);
        refreshToken.setExpiration(date.toString());

        refreshTokenRepository.save(refreshToken);
    }
}