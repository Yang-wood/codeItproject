package com.codeit.mini.dto.omr;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitTestDTO {
	
	private List<TestAnswerDTO> answers;
	private int duration;
}
