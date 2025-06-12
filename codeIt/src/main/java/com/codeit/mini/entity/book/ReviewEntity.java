package com.codeit.mini.entity.book;

import com.codeit.mini.entity.comm.BaseEntity;
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

@Entity
@SequenceGenerator(
			name = "REVIEW_SEQ_GEN",
			sequenceName = "review_seq",
			initialValue = 1,
			allocationSize = 1
		)
@Table(name = "review")
public class ReviewEntity extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVIEW_SEQ_GEN")
	private Long reviewId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rent_id")
	private RentEntity rentEntity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity memberEntity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private BookEntity bookEntity;
	
	@Column(name = "rating", nullable = false, columnDefinition = "NUMBER(2,1) CHECK (rating BETWEEN 0 AND 5)")
	private Double rating;
	
	@Column(name = "title", nullable = false, length = 300)
	private String title;
	
	@Column(name = "content", nullable = false, length = 500)
	private String content;
}
