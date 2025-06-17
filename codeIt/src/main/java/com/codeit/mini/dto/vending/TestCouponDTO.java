package com.codeit.mini.dto.vending;


import com.codeit.mini.entity.comm.CouponStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class TestCouponDTO {

	private Long testCouponId;
	private Long memberId;
	private Long itemId;
	private String couponCode;
	private Integer totalCnt;
	private Integer remainCnt;
	private CouponStatusEnum status;
	
}
