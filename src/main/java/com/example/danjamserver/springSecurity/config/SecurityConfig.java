package com.example.danjamserver.springSecurity.config;

import com.example.danjamserver.springSecurity.jwt.CustomLogoutFilter;
import com.example.danjamserver.springSecurity.jwt.JWTFilter;
import com.example.danjamserver.springSecurity.jwt.JWTUtil;
import com.example.danjamserver.springSecurity.jwt.LoginFilter;
import com.example.danjamserver.springSecurity.repository.RefreshTokenRepository;
import com.example.danjamserver.springSecurity.service.CustomUserDetailsService;
import com.example.danjamserver.springSecurity.service.UserBasicInformationService;
import com.example.danjamserver.util.exception.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationEntryPoint entryPoint;	// 추가
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final UserBasicInformationService userBasicInformationService;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil,
                          RefreshTokenRepository refreshTokenRepository,
                          CustomUserDetailsService customUserDetailsService, AuthenticationEntryPoint entryPoint, UserBasicInformationService userBasicInformationService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.entryPoint = entryPoint;
        this.accessDeniedHandler = new CustomAccessDeniedHandler();	// 추가
        this.userBasicInformationService = userBasicInformationService; //로그인시 사용자 정보를 응답하기 위한 서비스
    }

    // AuthenticationManager를 빈으로 등록한다.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    //비밀번호 암호화 메소드. 해당 메서드가 반환하는 객체가 빈으로 등록된다.
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterCHain(HttpSecurity http) throws Exception {

        //csrf disable. 세션방식에서는 항상 고정되기 때문에 방어해야 하지만, jwt방식은 stateless하기 때문에 disable해도 된다.
        http
                .csrf((auth) -> auth.disable());
        //Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //httpBasic 로그인 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/user/myProfile", "/api/user/myProfile/profileImg", "/api/user/college", "/api/user/nickname", "/api/user/account").authenticated()
                        .requestMatchers("/api/home/myschedule").authenticated() //홈 개인 일정
                        .requestMatchers("/api/home/fixed-myschedule").authenticated() // 홈 고정 일정
                        .requestMatchers("/api/user/myProfile/myInfo").authenticated() // 유저 프로필 수정 관련
                        .requestMatchers("/api/notice","/api/notice/*").authenticated() // 알림 관련
                        // 메이트 관련
                        .requestMatchers("/api/mate/{mateType}/condition").hasRole("AUTH_USER") //메이트 관련
                        .requestMatchers("/api/mate/filter").authenticated()
                        .requestMatchers("/api/mate/roommate/profile").hasRole("AUTH_USER")
                        .requestMatchers("/api/mate/roommate/candidate").hasRole("AUTH_USER")
                        .requestMatchers("/api/mate/roommate/candidate/basic").hasRole("AUTH_USER")
                        .requestMatchers("/api/mate/foodmate/candidate").hasRole("AUTH_USER")
                        .requestMatchers("/api/mate/food/profile").hasRole("AUTH_USER")
                        .requestMatchers("/api/mate/workout/profile").hasRole("AUTH_USER")
                        .requestMatchers("/api/mate/workout/candidate").hasRole("AUTH_USER")
                        .requestMatchers("/api/mate/walkmate/profile").hasRole("AUTH_USER")
                        .requestMatchers("/api/mate/walkmate/candidate").hasRole("AUTH_USER")
                        .requestMatchers("/api/mate/studymate/profile").hasRole("AUTH_USER")
                        .requestMatchers("/api/mate/studymate/candidate").hasRole("AUTH_USER")
                        .requestMatchers("/api/user/mateProfile/walkMate").hasRole("AUTH_USER")
                        .requestMatchers("/api/user/mateProfile/foodMate").hasRole("AUTH_USER")
                        .requestMatchers("/api/user/mateProfile/workoutMate").hasRole("AUTH_USER")
                        .requestMatchers("/api/user/mateProfile/roomMate").hasRole("AUTH_USER")
                        .requestMatchers("/api/user/mateProfile/studyMate").hasRole("AUTH_USER")
                        .requestMatchers("/api/user/myProfile/mate").hasRole("AUTH_USER")
                        .requestMatchers("/api/user/myProfile/mbti").hasRole("AUTH_USER")
                        .requestMatchers("/api/user/myProfile/school").hasRole("AUTH_USER")
                        .requestMatchers("/api/notice/topic/scribe").hasRole("AUTH_USER")
                        .requestMatchers("/api/notice/unScribe/scribe").hasRole("AUTH_USER")
                        .requestMatchers("/api/mateLikes/**").hasRole("AUTH_USER")
                        .requestMatchers("/api/user/report").hasRole("AUTH_USER")
                        .requestMatchers("/api/user/info").hasRole("AUTH_USER")
                        .requestMatchers("/api/user/block").hasRole("AUTH_USER")
                        .requestMatchers("/api/admin/report/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/block/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/swagger-ui/**", "/chat/**", "/api/chat-rooms/**").permitAll() //swagger 관련
                        .requestMatchers("/v3/**").permitAll() //swagger 관련
                        .requestMatchers("/api/mate/roommate/profile").authenticated()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        //스웨거 관련
                        .anyRequest().permitAll());

        //예외처리 핸들러 추가
        http
                .exceptionHandling(handler -> handler
                    .authenticationEntryPoint(entryPoint)
                    .accessDeniedHandler(accessDeniedHandler));

        //로그인 필터 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil, customUserDetailsService), LoginFilter.class);

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenRepository, userBasicInformationService), UsernamePasswordAuthenticationFilter.class);

        //로그아웃 필터 추가
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}