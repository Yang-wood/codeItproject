package com.codeit.mini.dto.vending;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PointHistoryDTO {

	private Long pointId;
	private Long memberId;
	private Integer amount;
	private String type;
	private String reason;
	private LocalDateTime regDate;
}
