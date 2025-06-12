package com.codeit.mini.entity.omr;

import org.hibernate.annotations.Check;

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
		name = "TEST_ANSWER_SEQ_GEN",
		sequenceName = "test_answer_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Check(constraints = "choice_answer IN ('A', 'B', 'C', 'D')")
@Table(name = "test_answer")
public class TestAnswerEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "TEST_ANSWER_SEQ_GEN")
	@Column(name = "answer_id")
	private Long answerId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = false)
	private TestSessionEntity sessionId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false)
	private TestQuestionEntity questionId;
	
	@Column(name = "choice_answer", columnDefinition = "CHAR(1)")
	private char choiceAnswer;
	
/*	
	public TestAnswerEntity toDto(TestAnswerDTO testAnswerDto) {
    	
    }
*/	
	
}
