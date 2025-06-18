package com.codeit.mini.service.omr;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.dto.omr.TestQuestionDTO;
import com.codeit.mini.entity.omr.TestEntity;
import com.codeit.mini.entity.omr.TestQuestionEntity;

public interface ITestQuestionService {
	
	
    Long register(TestQuestionDTO dto);
    TestQuestionDTO findById(Long questionId);
    List<TestQuestionDTO> getList();
    List<TestQuestionDTO> getListByTestId(Long testId);
    void update(TestQuestionDTO dto);
    void delete(Long questionId);
    
    
    List<TestQuestionDTO> getRandomQuestions(Long testId);
    
    // DB정답 가져오기
    int getCorrectAnswerFromDB(Long questionId);
    
    
    // 문제ID로 시험ID 가져오기
    Long getTestIdByQuestion(Long questionId);
    
    
    
    default TestQuestionEntity dtoToEntity(TestQuestionDTO dto) {
        return TestQuestionEntity.builder()
        		.questionId(dto.getQuestionId())
                .testId(TestEntity.builder().testId(dto.getTestId()).build())
                .questionText(dto.getQuestionText())
                .answer(dto.getAnswer())
                .explanation(dto.getExplanation())
                .choiceJson(dto.getChoiceJson())
                .build();
    }


    
    default TestQuestionDTO entityToDto(TestQuestionEntity entity) {
        return TestQuestionDTO.builder()
        		.questionId(entity.getQuestionId())
                .testId(entity.getTestId().getTestId())
                .questionText(entity.getQuestionText())
                .answer(entity.getAnswer())
                .explanation(entity.getExplanation())
                .choiceJson(entity.getChoiceJson())
                .build();
    }
}
