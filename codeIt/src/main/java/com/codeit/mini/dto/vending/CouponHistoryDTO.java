package com.codeit.mini.dto.vending;

import java.time.LocalDateTime;

import com.codeit.mini.entity.comm.CouponStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponHistoryDTO {

	private Long couponId;
	private Long memberId;
	private Long itemId;
	private String couponCode;
	private String couponType;
	private CouponStatusEnum status;
	private LocalDateTime issuedDate;
	private LocalDateTime usedDate;
	private LocalDateTime expireDate;
}  
