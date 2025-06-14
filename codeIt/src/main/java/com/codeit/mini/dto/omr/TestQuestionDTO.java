package com.codeit.mini.dto.omr;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestQuestionDTO {
	
	private Long questionId;
	
	private Long testId;
	
	private String questionText;
	
	private char answer;
	
	private String explanation;
	
	private String choiceJson;
}
