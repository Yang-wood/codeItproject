package com.codeit.mini.dto.vending;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCouponHistoryDTO {
	
	private Long historyId;

	private Long couponId;
	private String couponCode;

	private Long memberId;

	private String status;
	private String description;

	private LocalDateTime regDate;
	private LocalDateTime usedDate;
}
