package com.codeit.mini.service.omr;

import java.util.List;

import com.codeit.mini.dto.omr.TestAnswerDTO;
import com.codeit.mini.dto.omr.TestAnswerDetailDTO;
import com.codeit.mini.entity.omr.TestAnswerEntity;
import com.codeit.mini.entity.omr.TestQuestionEntity;
import com.codeit.mini.entity.omr.TestSessionEntity;

public interface ITestAnswerService {
	
	Long register(TestAnswerDTO dto);
    TestAnswerDTO findById(Long answerId);
    List<TestAnswerDTO> getList();
    void update(TestAnswerDTO dto);
    void delete(Long answerId);
    List<TestAnswerDTO> getAnswersBySessionId(Long sessionId);
    List<TestAnswerDTO> getAnswersByQuestionId(Long questionId);
	
    
    List<TestAnswerDetailDTO> getAnswerDetail(Long sessionId);
    
    
    
    
    default TestAnswerDTO entityToDto(TestAnswerEntity entity) {
        return TestAnswerDTO.builder()
            .answerId(entity.getAnswerId())
            .sessionId(entity.getSessionId().getSessionId())
            .questionId(entity.getQuestionId().getQuestionId())
            .choiceAnswer(entity.getChoiceAnswer())
            .build();
    }

    default TestAnswerEntity dtoToEntity(TestAnswerDTO dto, TestSessionEntity session, TestQuestionEntity question) {
        return TestAnswerEntity.builder()
            .answerId(dto.getAnswerId())
            .sessionId(session)
            .questionId(question)
            .choiceAnswer(dto.getChoiceAnswer())
            .build();
    }
    
    
}
