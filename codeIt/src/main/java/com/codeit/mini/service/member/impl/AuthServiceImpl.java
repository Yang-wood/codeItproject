package com.codeit.mini.service.member.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.mini.entity.member.EmailAuthEntity;
import com.codeit.mini.repository.member.EmailAuthRepository;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.service.member.AuthService;
import com.codeit.mini.service.member.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

	private final EmailService emailService;
    private final EmailAuthRepository emailAuthRepository;
    private final IMemberRepository memberRepository;

    @Override
    public String generateAuthCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @Override
    @Transactional
    public void sendAuthEmail(String email) {
        String code = generateAuthCode();

        EmailAuthEntity auth = EmailAuthEntity.builder()
                .email(email)
                .authCode(code)
                .expireTime(LocalDateTime.now().plusMinutes(5))
                .build();

        emailAuthRepository.save(auth);

        String subject = "[CODEIT] 이메일 인증 코드 안내";
        String body = "인증 코드: " + code + "\n5분 내에 입력해주세요.";
        emailService.sendEmail(email, subject, body);

        log.info("✅ 인증 코드 전송 완료: {}", code);
    }

    @Override
    @Transactional
    public boolean verifyAuthCode(String email, String code) {
    	
    	Optional<EmailAuthEntity> result = emailAuthRepository.findById(email);

        if (result.isPresent()) {
            EmailAuthEntity auth = result.get();

            if (!auth.isExpired() && auth.getAuthCode().equals(code)) {
                // ✅ verified 필드를 true로 변경
                auth.setVerified(true);
                emailAuthRepository.save(auth);  // 수정된 인증 정보 저장

                log.info("✅ 인증 코드 일치 및 verified 처리 완료");
                return true;
            }
        }

        return false;
    }
}
