package com.codeit.mini.dto.omr;


import com.codeit.mini.entity.omr.CategoryEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
	
	private Long categoryId;
	
	private String categoryType;
	
	
	public CategoryDTO toDto(CategoryEntity categoryEntity) {
		
		return CategoryDTO.builder().categoryId(categoryEntity.getCategoryId())
									.categoryType(categoryEntity.getCategoryType())
									.build();
	}
	
	
	public CategoryEntity toEntity(CategoryDTO categoryDTO) {
		
		return CategoryEntity.builder().categoryId(categoryDTO.getCategoryId())
									   .categoryType(categoryDTO.getCategoryType())
									   .build();
	}
	
	
}









