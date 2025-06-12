package com.codeit.mini.entity.omr;

import com.codeit.mini.dto.omr.CategoryDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@SequenceGenerator(
		name = "CATEGORY_SEQ_GEN",
		sequenceName = "category_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "category")
public class CategoryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "CATEGORY_SEQ_GEN")
	@Column(name = "category_id")
	private Long categoryId;
	
	@Column(name = "category_type")
	private String categoryType;
	
	
	
	
	
	
	public CategoryDTO toDto(CategoryEntity categoryEntity) {
		
		return CategoryDTO.builder().categoryId(categoryEntity.getCategoryId())
									.categoryType(categoryEntity.getCategoryType())
									.build();
	}
}







