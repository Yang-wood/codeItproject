package com.codeit.mini.service.omr.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.omr.TempAnswerDTO;
import com.codeit.mini.entity.omr.TempAnswerEntity;
import com.codeit.mini.entity.omr.TestQuestionEntity;
import com.codeit.mini.entity.omr.TestSessionEntity;
import com.codeit.mini.repository.omr.ITempAnswerRepository;
import com.codeit.mini.repository.omr.ITestQuestionRepository;
import com.codeit.mini.repository.omr.ITestSessionRepository;
import com.codeit.mini.service.omr.ITempAnswerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class TempAnswerServiceImpl implements ITempAnswerService{
	
	private final ITempAnswerRepository answerRepository;
	private final ITestSessionRepository sessionRepository;
	private final ITestQuestionRepository questionRepository;
	
	
	@Override
	public Long register(TempAnswerDTO dto) {
		TestSessionEntity session = sessionRepository.findById(dto.getSessionId()).orElseThrow();
		TestQuestionEntity question = questionRepository.findById(dto.getQuestionId()).orElseThrow();
		
		TempAnswerEntity entity = dtoToEntity(dto, session, question);
		
		TempAnswerEntity saved = answerRepository.save(entity);
		log.info("저장된 답안 : {}", saved);
		
		return saved.getTempId();
	}

	@Override
	public TempAnswerDTO findById(Long tempId) {
		Optional<TempAnswerEntity> result = answerRepository.findById(tempId);
		log.info("조회된 답안 : {}", result);
		
		return result.map(this::entityToDto).orElse(null);
	}

	@Override
	public List<TempAnswerDTO> getList() {
		return answerRepository.findAll().stream()
											.map(this::entityToDto)
											.collect(Collectors.toList());
	}

	@Override
	public void update(TempAnswerDTO dto) {
		TempAnswerEntity entity = answerRepository.findById(dto.getTempId()).orElseThrow();
		
		entity.setChoiceAnswer(dto.getChoiceAnswer());
		entity.setSaveTime(dto.getSaveTime().toLocalDateTime());
		
		answerRepository.save(entity);
		
	}

	@Override
	public void delete(Long tempId) {
		answerRepository.deleteById(tempId);
		
	}

	@Override
	public List<TempAnswerDTO> getBySessionId(Long sessionId) {
		return answerRepository.findAll().stream()
								.filter(e -> e.getSessionId().getSessionId().equals(sessionId))
								.map(this::entityToDto)
								.collect(Collectors.toList());
	}

	@Override
	public List<TempAnswerDTO> getByQuestionId(Long questionId) {
		 return answerRepository.findAll().stream()
								.filter(e -> e.getQuestionId().getQuestionId().equals(questionId))
								.map(this::entityToDto)
								.collect(Collectors.toList());
	}

}
