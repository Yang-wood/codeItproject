package com.codeit.mini.entity.omr;

import org.hibernate.annotations.Check;

import com.codeit.mini.entity.admin.Admin;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id", nullable = false)
	private Admin adminId;
	
	@Column(name = "test_title", nullable = false, length = 100)
	private String testTitle;
	
	@Column(name = "test_desc", length = 1000)
	private String testDesc;
	
	@Column(name = "test_limit", nullable = false)
	private int testLimit;
	
	@Column(name = "test_paid", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
	private char testPaid = 'Y';
	
	@Column(name = "avg_score")
	private Double avgScore;
	
	@Column(name = "attempt_cnt")
	private int attemptCnt;
	
	@Column(name = "is_open", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
	private char isOpen = 'Y';
	
	@Column(name = "view_cnt", nullable = false)
	private int viewCnt = 0;
	
	
	public void changeTitle(String testTitle) {
		this.testTitle = testTitle;
	}
	
	
	public void changeDesc(String testDesc) {
		this.testDesc = testDesc;
	}
	
	public void changeIsOpen(char isOpen) {
		this.isOpen = isOpen;
	}
	
	
}








