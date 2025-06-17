package com.codeit.mini.controller.member;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.mini.service.member.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class AuthController {

	private final AuthService authService;

    @PostMapping("/send-auth-email")
    public String sendEmail(@RequestParam("email") String email) {
        authService.sendAuthEmail(email);
        return "인증 코드 전송 완료";
    }

    @PostMapping("/verify-auth-code")
    public String verifyEmailCode(@RequestParam("email") String email, 
    							  @RequestParam("code") String code) {
        boolean verified = authService.verifyAuthCode(email, code);
        return verified ? "인증 성공" : "인증 실패 또는 만료";
    }
}
