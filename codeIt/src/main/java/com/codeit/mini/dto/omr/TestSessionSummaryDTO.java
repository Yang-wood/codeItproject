package com.codeit.mini.dto.omr;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestSessionSummaryDTO {
	
	private Long sessionId;
	private Long memberId;
	private Long testId;
	private Timestamp submitTime;
	private Integer score;
	private Integer duration;
	private String categoryType;
	
	
	
	public TestSessionSummaryDTO(Long sessionId, Long memberId, Long testId, Timestamp submitTime, Integer score,
			Integer duration, String categoryType) {
		super();
		this.sessionId = sessionId;
		this.memberId = memberId;
		this.testId = testId;
		this.submitTime = submitTime;
		this.score = score;
		this.duration = duration;
		this.categoryType = categoryType;
	}
	
	
	
	
	
	
	
}
