package com.codeit.mini.entity.member;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "email_auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailAuthEntity {

	
	@Id
    @Column(length = 100)
    private String email; // member_email과 동일한 이메일

    @Column(length = 10, nullable = false)
    private String authCode;

    @Column(nullable = false)
    private LocalDateTime expireTime;
    
    @Column(nullable = false)
    private boolean verified;  // 인증 성공 여부

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expireTime);
    }
}
