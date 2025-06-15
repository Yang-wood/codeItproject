package com.codeit.mini.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
	
	private Long adminId;              // 관리자 ID (조회용)
    private String adminLoginId;       // 로그인 ID
    private String adminPw;            // 비밀번호
    private String adminName;          // 이름
    private String adminEmail;         // 이메일

}
