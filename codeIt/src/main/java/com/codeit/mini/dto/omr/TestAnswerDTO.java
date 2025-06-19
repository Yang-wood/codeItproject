package com.codeit.mini.dto.omr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestAnswerDTO {
	
	private Long answerId;
	private Long sessionId;
	private Long questionId;
	private Character choiceAnswer;
}
