package com.codeit.mini.service.omr.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.omr.TestAnswerDTO;
import com.codeit.mini.dto.omr.TestAnswerDetailDTO;
import com.codeit.mini.entity.omr.TestAnswerEntity;
import com.codeit.mini.entity.omr.TestQuestionEntity;
import com.codeit.mini.entity.omr.TestSessionEntity;
import com.codeit.mini.repository.omr.ITestAnswerRepository;
import com.codeit.mini.repository.omr.ITestQuestionRepository;
import com.codeit.mini.repository.omr.ITestSessionRepository;
import com.codeit.mini.service.omr.ITestAnswerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class TestAnswerServiceImpl implements ITestAnswerService{
	
	private final ITestAnswerRepository answerRepository;
	private final ITestSessionRepository sessionRepository;
	private final ITestQuestionRepository questionRepository;
	
	
	
	@Override
	public Long register(TestAnswerDTO dto) {
		TestSessionEntity session = sessionRepository.findById(dto.getSessionId()).get();
		TestQuestionEntity question = questionRepository.findById(dto.getQuestionId()).get();
		
		TestAnswerEntity answer = dtoToEntity(dto, session, question);
		
		TestAnswerEntity saved = answerRepository.save(answer);
		log.info("등록된 답안 : {}", saved);
		
		return saved.getAnswerId();
	}

	@Override
	public TestAnswerDTO findById(Long answerId) {
		Optional<TestAnswerEntity> result = answerRepository.findById(answerId);
		
		return result.map(this::entityToDto).orElse(null);
	}

	@Override
	public List<TestAnswerDTO> getList() {
		return answerRepository.findAll().stream().map(this::entityToDto).collect(Collectors.toList());
	}

	@Override
	public void update(TestAnswerDTO dto) {
		TestAnswerEntity entity = answerRepository.findById(dto.getAnswerId()).get();
		
		entity.setChoiceAnswer(dto.getChoiceAnswer());
		
		answerRepository.save(entity);
		
	}

	@Override
	public void delete(Long answerId) {
		answerRepository.deleteById(answerId);
		log.info("삭제된 답안ID : {}", answerId);
		
	}

	@Override
	public List<TestAnswerDTO> getAnswersBySessionId(Long sessionId) {
		return answerRepository.findBySessionId_SessionId(sessionId)
										.stream()
										.map(this::entityToDto)
										.collect(Collectors.toList());
	}

	@Override
	public List<TestAnswerDTO> getAnswersByQuestionId(Long questionId) {
		return answerRepository.findByQuestionId_QuestionId(questionId)
										.stream()
										.map(this::entityToDto)
										.collect(Collectors.toList());
	}

	@Override
	public List<TestAnswerDetailDTO> getAnswerDetail(Long sessionId) {
		List<TestAnswerDetailDTO> result = answerRepository.findAnswerDetailBySessionId(sessionId);
		
		ObjectMapper mapper = new ObjectMapper();
		
		for (TestAnswerDetailDTO testAnswerDetailDTO : result) {
			try {
				Map<String, String> map = mapper.readValue(testAnswerDetailDTO.getChoiceJson(), 
														   new TypeReference<Map<String, String>>() {});
				testAnswerDetailDTO.setChoiceMap(map);
			} catch (Exception e) {
				e.printStackTrace();
				testAnswerDetailDTO.setChoiceMap(Collections.emptyMap());
			}
		}
		
		return result;
		
	}

}
