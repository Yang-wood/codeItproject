package com.codeit.mini.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor{

	 @Override
	    public boolean preHandle(HttpServletRequest request,
	                             HttpServletResponse response,
	                             Object handler) throws Exception {

	        HttpSession session = request.getSession(false);

	        if (session == null || (session.getAttribute("member") == null && session.getAttribute("admin") == null)) {
	            String ajaxHeader = request.getHeader("X-Requested-With");
	            String accept = request.getHeader("Accept");

	            if ("XMLHttpRequest".equals(ajaxHeader) || (accept != null && accept.contains("application/json"))) {
	                response.setContentType("application/json;charset=UTF-8");
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.getWriter().write("{\"message\":\"로그인이 필요합니다.\"}");
	            } else {
	                response.sendRedirect("/member/login");
	            }

	            return false;
	        }

	        return true;
	    }
}
