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
}
