package com.codeit.mini.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer{
	// AuthInterceptor를 전체 웹 애플리케이션에 적용
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/member/**") // /member/로 시작하는 모든 요청에 대해 검사 적용
                .excludePathPatterns("/member/login", "/member/register", "/member/checkId", "/main", "/error"); // 예외 처리
    }
}
