package com.codeit.mini.service.omr;

import java.util.List;

import com.codeit.mini.dto.omr.CategoryDTO;
import com.codeit.mini.entity.omr.CategoryEntity;

public interface ICategoryService {
	
	CategoryDTO register(CategoryDTO categoryDTO);
	CategoryDTO update(CategoryDTO categoryDTO);
	void delete(Long categoryId);
	List<CategoryDTO> getList();
	CategoryDTO findById(Long categoryId);
	
	
	
	default CategoryDTO toDto(CategoryEntity categoryEntity) {
		
		return CategoryDTO.builder().categoryId(categoryEntity.getCategoryId())
									.categoryType(categoryEntity.getCategoryType())
									.build();
	}
	
	
	default CategoryEntity toEntity(CategoryDTO categoryDTO) {
		
		return CategoryEntity.builder().categoryId(categoryDTO.getCategoryId())
									   .categoryType(categoryDTO.getCategoryType())
									   .build();
	}
}
