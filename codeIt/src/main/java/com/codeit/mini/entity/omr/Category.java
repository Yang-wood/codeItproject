package com.codeit.mini.entity.omr;

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
@ToString
@Table(name = "category")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "CATEGORY_SEQ_GEN")
	private Long categoryId;
	
	private String categoryType;
}







