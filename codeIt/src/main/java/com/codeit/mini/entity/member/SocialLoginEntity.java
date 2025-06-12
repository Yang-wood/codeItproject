package com.codeit.mini.entity.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_login")
@SequenceGenerator(
		name = "SOCIAL_LOGIN_SEQ_GEN",
		sequenceName = "social_login_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SocialLoginEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "SOCIAL_LOGIN_SEQ_GEN")
	@Column(name = "Social_login_id")
	private Long socialLoginId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private MemberEntity member;
	
	@Column(name = "provider", nullable = false, length = 20)
    private String provider;
	
	@Column(name = "provider_member_id", nullable = false, length = 100)
    private String providerMemberId;
}
