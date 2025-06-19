package com.codeit.mini.dto.omr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartTestDTO {
	
	private Long memberId;
	private Long testId;
	private Long itemId;
	private String couponCode;
}
