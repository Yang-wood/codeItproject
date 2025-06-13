package com.codeit.mini.dto.omr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestDTO {
	
	private Long testId;
	private Long categoryId;
	private Long adminId;
	private String testTitle;
	private String testDesc;
	private int testLimit;
	private char testPaid;
	private Double avgScore;
	private int attemptCnt;
	private char isOpen;
	private int viewCnt;
	
	
}
