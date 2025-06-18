package com.codeit.mini.service.omr;

import java.sql.Timestamp;
import java.util.List;

import com.codeit.mini.dto.omr.TempAnswerDTO;
import com.codeit.mini.entity.omr.TempAnswerEntity;
import com.codeit.mini.entity.omr.TestQuestionEntity;
import com.codeit.mini.entity.omr.TestSessionEntity;

public interface ITempAnswerService {

    Long register(TempAnswerDTO dto);
    TempAnswerDTO findById(Long tempId);
    List<TempAnswerDTO> getList();
    void update(TempAnswerDTO dto);
    void delete(Long tempId);
    List<TempAnswerDTO> getBySessionId(Long sessionId);
    List<TempAnswerDTO> getByQuestionId(Long questionId);

    
    
    
    
    default TempAnswerDTO entityToDto(TempAnswerEntity entity) {
        return TempAnswerDTO.builder()
            .tempId(entity.getTempId())
            .sessionId(entity.getSessionId().getSessionId())
            .questionId(entity.getQuestionId().getQuestionId())
            .choiceAnswer(entity.getChoiceAnswer())
            .saveTime(entity.getSaveTime() != null ? Timestamp.valueOf(entity.getSaveTime()) : null)
            .build();
    }

    default TempAnswerEntity dtoToEntity(TempAnswerDTO dto, 
    									TestSessionEntity session, TestQuestionEntity question) {
    	
        return TempAnswerEntity.builder()
            .tempId(dto.getTempId())
            .sessionId(session)
            .questionId(question)
            .choiceAnswer(dto.getChoiceAnswer())
            .saveTime(dto.getSaveTime() != null ? dto.getSaveTime().toLocalDateTime() : null)
            .build();
    }
	
	
}
