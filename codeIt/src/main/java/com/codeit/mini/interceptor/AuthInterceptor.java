package com.codeit.mini.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor{
	// "로그인 여부를 검사"해서 로그인 안 한 사용자는 로그인 페이지로 리다이렉트시킴
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        
        System.out.println("AuthInterceptor 작동 중! 요청 URI: " + request.getRequestURI());
        
        if (member == null) {
            // 로그인 안 된 경우
            response.sendRedirect(request.getContextPath() + "/member/login");
            return false;
        }

        return true;
    }
}
