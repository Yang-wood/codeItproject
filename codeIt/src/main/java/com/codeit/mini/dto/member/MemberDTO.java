package com.codeit.mini.dto.member;

public class MemberDTO {

	private Long memberId;         // 조회 및 세션 처리
    private String loginId;        // 로그인 및 가입
    private String memberPw;       // 로그인 및 가입
    private String memberName;     // 가입, 조회
    private String memberEmail;    // 가입, 조회
    private char emailVerified;    // 로그인 시 체크
    private char termsAgreed;      // 가입 시만 사용
    private int status;            // 관리 용도
    private int role;              // 권한
    private int points;            // 포인트
    private int coupon;            // 쿠폰 수
}
