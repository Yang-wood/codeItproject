package com.codeit.mini.dto.vending;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class CouponHistoryDTO {

	private Long couponId;
	private Long memberId;
	private Long itemId;
	private String couponCode;
	private String couponType;
	private String status;
	private LocalDateTime issuedDate;
	private LocalDateTime usedDate;
	private LocalDateTime expireDate;
}  
