package com.codeit.mini.entity.book;

import java.time.LocalDateTime;

import com.codeit.mini.entity.member.MemberEntity;

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
import lombok.Setter;
import lombok.ToString;

@Builder
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
@Setter
@ToString
public class RentEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENT_SEQ_GEN")
	private Long rentId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private BookEntity bookEntity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity memberEntity;
	
	@Column(name = "rent_date", nullable = false)
	private LocalDateTime rentDate;
	
	@Column(name = "return_date", nullable = false)
	private LocalDateTime returnDate;
	
	@Column(name = "is_returned", nullable = false, columnDefinition = "NUMBER(1) DEFAULT 0 CHECK (is_returned IN (0, 1))")
	private Integer isReturned = 0;
	
	@Column(name = "read_page", nullable = false, columnDefinition = "NUMBER DEFAULT 0")
	private Integer readPage = 0;
	
	@Column(name = "read_state", nullable = false, columnDefinition = "NUMBER(1) DEFAULT 0 CHECK (read_state IN (0, 1))")
	private Integer readState = 0;
	
	@Column(name = "has_review", nullable = false, columnDefinition = "NUMBER(1) DEFAULT 0 CHECK (has_review IN (0, 1))")
	private Integer hasReview = 0;
	
	@Column(name = "point_get", nullable = false, columnDefinition = "NUMBER(1) DEFAULT 0 CHECK (point_get IN (0, 1))")
	private Integer pointGet = 0;
	
}
