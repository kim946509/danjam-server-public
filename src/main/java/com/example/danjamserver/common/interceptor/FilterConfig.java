package com.example.danjamserver.common.interceptor;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<ServletWrappingFilter> servletWrappingFilterRegistration() {
        FilterRegistrationBean<ServletWrappingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ServletWrappingFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1); // Spring Security 필터보다 먼저 실행되도록 설정
        return registration;
    }
}

