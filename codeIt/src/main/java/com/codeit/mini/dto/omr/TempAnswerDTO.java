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
public class TempAnswerDTO {
	
	private Long tempId;
	private Long sessionId;
	private Long questionId;
	private char choiceAnswer;
	private Timestamp saveTime;
	
}
