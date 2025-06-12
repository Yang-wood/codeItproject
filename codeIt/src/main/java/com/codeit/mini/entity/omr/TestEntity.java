package com.codeit.mini.entity.omr;

import org.hibernate.annotations.Check;

import com.codeit.mini.entity.comm.BaseEntity;

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

@Entity
@SequenceGenerator(
		name = "TEST_SEQ_GEN",
		sequenceName = "test_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Check(constraints = "test_paid IN ('Y', 'N') AND is_open IN ('Y', 'N')")
@Table(name = "test")
public class TestEntity extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "TEST_SEQ_GEN")
	private Long testId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private CategoryEntity categoryId;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "admin_id", nullable = false)
//	private Long adminId;
	
	@Column(name = "test_title", nullable = false)
	private String testTitle;
	
	@Column(name = "test_desc")
	private String testDesc;
	
	@Column(name = "test_limit", nullable = false)
	private int testLimit;
	
	@Column(name = "test_paid", nullable = false)
	private char testPaid;
	
	@Column(name = "avg_score")
	private double avgScore;
	
	@Column(name = "attempt_cnt")
	private int attemptCnt;
	
	@Column(name = "is_open")
	private char isOpen;
	
	@Column(name = "view_cnt")
	private int viewCnt;
}








