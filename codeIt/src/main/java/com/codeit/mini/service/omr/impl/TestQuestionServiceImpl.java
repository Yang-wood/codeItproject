package com.codeit.mini.service.omr.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeit.mini.dto.omr.TestQuestionDTO;
import com.codeit.mini.entity.omr.TestQuestionEntity;
import com.codeit.mini.repository.omr.ITestQuestionRepository;
import com.codeit.mini.service.omr.ITestQuestionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Service
@RequiredArgsConstructor
@Log4j2
public class TestQuestionServiceImpl implements ITestQuestionService{
	
	private final ITestQuestionRepository questionRepository;
	
	
	@Override
	public Long register(TestQuestionDTO dto) {
		TestQuestionEntity entity = dtoToEntity(dto);
		TestQuestionEntity saved = questionRepository.save(entity);
		
		log.info("등록된 문제 : {}", saved);
		
		return saved.getQuestionId();
	}

	@Override
	public TestQuestionDTO findById(Long questionId) {
		Optional<TestQuestionEntity> result = questionRepository.findById(questionId);
		
		return result.map(this::entityToDto).orElse(null);
	}

	@Override
	public List<TestQuestionDTO> getList() {
		List<TestQuestionEntity> entityList = questionRepository.findAll();
		
		return entityList.stream().map(this::entityToDto).collect(Collectors.toList());
	}

	@Override
	public List<TestQuestionDTO> getListByTestId(Long testId) {
		List<TestQuestionEntity> entityList = questionRepository.findByTestId(testId);
		
		return entityList.stream().map(this::entityToDto).collect(Collectors.toList());
	}

	@Override
	public void update(TestQuestionDTO dto) {
		Optional<TestQuestionEntity> result = questionRepository.findById(dto.getQuestionId());
		
		if (result.isPresent()) {
			TestQuestionEntity entity = result.get();
			
			entity.changeText(dto.getQuestionText());
			entity.changeAnswer(dto.getAnswer());
			entity.changeExplanation(dto.getExplanation());
			entity.changeJson(dto.getChoiceJson());
			
			questionRepository.save(entity);
			log.info("수정된 문제 : {}", entity);
		} else {
			log.info("해당 문제를 찾을 수 없습니다");
		}
	}

	@Override
	public void delete(Long questionId) {
		if (questionRepository.existsById(questionId)) {
			questionRepository.deleteById(questionId);
			
			log.info("삭제된 문제 Id : {}", questionId);
		} else {
			log.info("해당 문제를 찾을 수 없습니다");
		}
	}

}
