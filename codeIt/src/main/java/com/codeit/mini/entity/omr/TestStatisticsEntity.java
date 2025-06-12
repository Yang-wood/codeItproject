package com.codeit.mini.entity.omr;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Check;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
		name = "TEST_STATISTICS_SEQ_GEN",
		sequenceName = "test_statistics_seq",
		initialValue = 1,
		allocationSize = 1
)
@EntityListeners(value = {AuditingEntityListener.class})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "test_statistics")
public class TestStatisticsEntity {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    				generator = "TEST_STATISTICS_SEQ_GEN")
    @Column(name = "stat_id")
    private Long statId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private TestEntity testId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private TestQuestionEntity questionId;

    @Column(name = "correct_rate")
    private Double correctRate;

    @Column(name = "attempts")
    private Long attempts;

    @Column(name = "correct_cnt")
    private Long correctCnt;
    
    @LastModifiedDate
	@Column(name = "updatedate", columnDefinition = "DATE DEFAULT SYSDATE")
    private LocalDate updateDate;
}
