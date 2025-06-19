package com.codeit.mini.dto.omr;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestSessionDTO {
	
	private Long sessionId;
	private Long memberId;
	private Long testId;
	private Timestamp startTime;
	private Timestamp submitTime;
	private Integer score;
	private Integer duration;
	private char isSubmited;
}
