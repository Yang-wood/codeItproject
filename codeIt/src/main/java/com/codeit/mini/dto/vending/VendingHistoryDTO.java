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
public class VendingHistoryDTO {

	private Long historyId;
	private Long memberId;
	private Long itemId;
	private String payment;
	private String status;
	private Long pointId;
	private Long couponId;
	private LocalDateTime regDate;
	
	private String paymentFilter;
	private String statusFilter;
	private Long machineIdFilter;

	private String itemName;
	private String couponCode;
}
