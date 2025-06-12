package com.codeit.mini.entity.member;

import java.time.LocalDateTime;

import org.hibernate.annotations.Check;

import com.codeit.mini.entity.comm.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@SequenceGenerator(
		name = "MEMBER_SEQ_GEN",
		sequenceName = "member_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "member")
@Check(constraints = "status In (0,1,2) AND role In (0,1) AND email_verified IN ('Y','N') AND terms_agreed IN ('Y', 'N')")
public class MemberEntity extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
							   generator = "MEMBER_SEQ_GEN")
	@Column(name = "member_id", columnDefinition = "NUMBER(10)" )
	private Long memberId;
	
	@Column(name = "login_id", nullable = false, unique = true, length = 30)
	private String loginId;
	
	@Column(name = "member_pw", nullable = false, length = 200)
	private String memberPw;
	
	@Column(name = "member_name", nullable = false, length = 50)
	private String memberName;
	
	@Column(name = "member_email", nullable = false, unique = true, length = 100)
	private String memberEmail;
	
	@Column(name = "email_verified", nullable = false, length = 1, columnDefinition = "CHAR(1) DEFAULT 'N'")
	private char emailVerified = 'N'; // 기본값
	
	@Column(name = "terms_agreed", nullable = false, length = 1, columnDefinition = "CHAR(1) DEFAULT 'N'")
	private char termsAgreed = 'N';
	
	@Column(name = "last_login")
	private LocalDateTime lastLogin;
	
	@Column(name = "status", nullable = false, columnDefinition = "NUMBER(4) DEFAULT 0")
	private int status = 0;
	
	@Column(name = "points", nullable = false, columnDefinition = "NUMBER(8) DEFAULT 0")
	private int points = 0;
	
	@Column(name = "coupon", nullable = false, columnDefinition = "NUMBER(4) DEFAULT 0")
	private int coupon = 0;
	
	@Column(name = "role", nullable = false, columnDefinition = "NUMBER(4) DEFAULT 0")
	private int role = 0;
}
