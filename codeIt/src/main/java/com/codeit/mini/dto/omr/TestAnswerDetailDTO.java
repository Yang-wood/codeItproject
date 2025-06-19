package com.codeit.mini.dto.omr;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestAnswerDetailDTO {
	
	private long questionId;
	private String questionText;
	private char choiceAnswer;
	private String choiceJson;
	private String explanation;
	
	private Map<String, String> choiceMap;

	
	
	
	
	
	public TestAnswerDetailDTO(long questionId, String questionText, char choiceAnswer, String choiceJson,
			String explanation) {
		super();
		this.questionId = questionId;
		this.questionText = questionText;
		this.choiceAnswer = choiceAnswer;
		this.choiceJson = choiceJson;
		this.explanation = explanation;
	}
	
	
	
	
	
}
