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
                .addPathPatterns("/member/**") // 보호할 경로
                .excludePathPatterns(
                		"/member/login",
                        "/member/register",
                        "/member/checkId",
                        "/member/send-auth-email",       // 인증코드 전송 제외
                        "/member/verify-auth-code",      // 인증코드 검증 제외
                        "/member/ajax/find-id",        	 // 아이디 찾기 예외 
                        "/member/ajax/find-pw",
                        "/member/ajax/update-name",
                        "/member/ajax/update-email",
                        "/member//ajax/update-password",
                        "/main",
                        "/error"
                );
		registry.addInterceptor(new LoginCheckInterceptor())
		        .addPathPatterns("/vendingmachines/**"); // 회원용 자판기 API 전부 보호
		
		registry.addInterceptor(new AdminCheckInterceptor())
		.addPathPatterns("/admin/**")
        .excludePathPatterns("/admin/login", "/admin/logout"); 
    }
}
