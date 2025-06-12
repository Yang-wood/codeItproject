package com.codeit.mini.entity.book;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(
			name = "RENT_SEQ_GEN",
			sequenceName = "rent_seq",
			initialValue = 1,
			allocationSize = 1
		)
@Table(name = "rent")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RentEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENT_SEQ_GEN")
	private Long review_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private BookEntity bookEntity;
	
	
	@Column(name = "rent_date", nullable = false)
	private LocalDateTime rent_date;
	
	@Column(name = "return_date", nullable = false)
	private LocalDateTime return_date;
	
	@Column(name = "is_returned", nullable = false)
	private Integer is_returned = 0;
	
	@Column(name = "read_page", nullable = false)
	private Long read_page = 0L;
	
	@Column(name = "read_state", nullable = false)
	private Integer read_state = 0;
	
	@Column(name = "has_review", nullable = false)
	private Integer has_review = 0;
}
