package com.codeit.mini.service.omr.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.omr.CategoryDTO;
import com.codeit.mini.entity.omr.CategoryEntity;
import com.codeit.mini.repository.omr.ICategoryRepository;
import com.codeit.mini.service.omr.ICategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService{
	
	private final ICategoryRepository categoryRepository;
	
	
	@Override
	public CategoryDTO register(CategoryDTO categoryDTO) {
		
		CategoryEntity entity = toEntity(categoryDTO);
		CategoryEntity regEntity = categoryRepository.save(entity);
		
		return toDto(regEntity);
	}

	@Override
	public CategoryDTO update(CategoryDTO categoryDTO) {
		Optional<CategoryEntity> optional = categoryRepository.findById(categoryDTO.getCategoryId());
		
		if (optional.isPresent()) {
			CategoryEntity entity = optional.get();
			entity.setCategoryType(categoryDTO.getCategoryType());
			
			CategoryEntity updateEntity = categoryRepository.save(entity);
			
			return toDto(updateEntity);
		}
		
		return null;
	}

	@Override
	public void delete(Long categoryId) {
		categoryRepository.deleteById(categoryId);
	}

	@Override
	public List<CategoryDTO> getList() {
		List<CategoryEntity> entityList = categoryRepository.findAll();
		
		return entityList.stream().map(entity -> toDto(entity))
								  .collect(Collectors.toList());
	}

	@Override
	public CategoryDTO findById(Long categoryId) {
		Optional<CategoryEntity> optional = categoryRepository.findById(categoryId);
		
		return optional.map(entity -> toDto(entity)).orElse(null);
	}

}
