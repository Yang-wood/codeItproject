package com.codeit.mini.service.omr.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.omr.TestDTO;
import com.codeit.mini.entity.omr.TestEntity;
import com.codeit.mini.repository.omr.ITestRepository;
import com.codeit.mini.service.omr.ITestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements ITestService{
	
	private final ITestRepository testRepository;

	@Override
	public Long register(TestDTO dto) {
		TestEntity entity = dtoToEntity(dto);
		
		testRepository.save(entity);
		
		return entity.getTestId();
	}

	@Override
	public TestDTO findById(Long testId) {
		return testRepository.findById(testId).map(this::entityToDto)
											  .orElse(null);
	}

	@Override
	public List<TestDTO> getList() {
		return testRepository.findAll().stream().map(this::entityToDto)
												.collect(Collectors.toList());
	}

	@Override
	public void update(TestDTO dto) {
		TestEntity entity = testRepository.findById(dto.getTestId()).orElseThrow();
		
		entity.setTestTitle(dto.getTestTitle());
		entity.setTestDesc(dto.getTestDesc());
		entity.setIsOpen(dto.getIsOpen());
		
		testRepository.save(entity);
	}

	@Override
	public void delete(Long testId) {
		testRepository.deleteById(testId);
	}
	
	

}
