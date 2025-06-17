package com.codeit.mini.service.member;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

private final JavaMailSender javaMailSender;
	
	public void sendEmail(String toEmail, String subject, String body) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		message.setFrom("wlgus990904@gmail.com");
		
		javaMailSender.send(message);
	}
	
	public void sendAuthCode(String toEmail, String code) {
	    
		String subject = "[CODEIT] 이메일 인증 코드 안내";
	    String body = "인증 코드: " + code + "\n5분 내에 입력해 주세요.";

	    sendEmail(toEmail, subject, body);
	}
	
	public void sendTempPassword(String toEmail, String tempPw) {
	    
		String subject = "[CODEIT] 임시 비밀번호 발급 안내";
	    String body = "요청하신 임시 비밀번호는 다음과 같습니다:\n\n" + tempPw +
	                  "\n\n로그인 후 반드시 비밀번호를 변경해주세요.";
	    
	    sendEmail(toEmail, subject, body);
	}
}
