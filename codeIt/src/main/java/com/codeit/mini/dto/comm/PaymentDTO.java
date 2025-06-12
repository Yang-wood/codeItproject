package com.codeit.mini.dto.comm;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PaymentDTO {

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
	private int amount;
	private LocalDateTime regDate;
	
}
