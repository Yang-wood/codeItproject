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
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(
			name = "BEST_SEQ_GEN",
			sequenceName = "best_seq",
			initialValue = 1,
			allocationSize = 1
		)
@Table(name = "bset")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BestEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BEST_SEQ_GEN")
	private Long best_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private BookEntity book;
	
	@Column(name = "score", nullable = false, precision = 10, scale = 2)
    private Double score;
	
	@Column(name = "regdate", nullable = false)
    private LocalDateTime regdate;

    @PrePersist
    public void prePersist() {
        if (regdate == null) {
        	regdate = LocalDateTime.now();
        }
    }
	
	
}
