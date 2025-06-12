package com.codeit.mini.entity.comm;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@SequenceGenerator(
		name = "PAYMENT_SEQ_GEN",
		sequenceName = "payment_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Payment {
	
	@Id
	private Long paymentId;
	
	private Long memberId;
	
	private String paymentType;
	
	private Long targetId;
	
	private String method;
	
	private String status;
	
	private String couponType;
	
	private Long couponId;
	
	private Long testCouponId;
	
	private Long pointId;
	
	private Long amount;
	
	@CreatedDate
	@Column(name = "regdate", updatable = false)
	private LocalDateTime regDate;
}
