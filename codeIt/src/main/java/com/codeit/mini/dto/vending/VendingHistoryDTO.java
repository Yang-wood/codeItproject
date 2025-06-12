package com.codeit.mini.dto.vending;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class VendingHistoryDTO {

	private Long historyId;
	private Long memberId;
	private Long ItemId;
	private String payment;
	private String status;
	private Long pointId;
	private Long couponId;
	private LocalDateTime regDate;
}
