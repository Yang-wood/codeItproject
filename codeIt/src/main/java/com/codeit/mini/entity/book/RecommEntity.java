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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(
			name = "RECOMM_SEQ_GEN",
			sequenceName = "recomm_seq",
			initialValue = 1,
			allocationSize = 1
		)
@Table(name = "recomm")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecommEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECOMM_SEQ_GEN")
	private Long recId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private BookEntity bookEntity;
	
	@Column(name = "rec_score", nullable = false, columnDefinition = "NUMBER(2,1)")
	private Double recScore;
	
	@Column(name = "regdate", nullable = false)
	private LocalDateTime regDate;
	
	public void prePersist() {
		if (regDate == null) {
			regDate = LocalDateTime.now();
		}
	}
}
