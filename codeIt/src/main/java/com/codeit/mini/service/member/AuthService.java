package com.codeit.mini.service.member;

public interface AuthService {

	String generateAuthCode();

    void sendAuthEmail(String email);

    boolean verifyAuthCode(String email, String code);
}
