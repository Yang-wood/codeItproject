package com.codeit.mini.entity.omr;

import org.hibernate.annotations.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
		name = "TEST_QUESTION_SEQ_GEN",
		sequenceName = "test_question_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "testId")
@Check(constraints = "answer IN ('1', '2', '3', '4')")
@Table(name = "test_question")
public class TestQuestionEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_SEQ_GEN")
    @Column(name = "question_id")
	private Long questionId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
	private TestEntity testId;
	
	@Column(name = "question_text", nullable = false, length = 2000)
	private String questionText;
	
	@Column(name = "answer", nullable = false, length = 1)
	private char answer;
	
	@Column(name = "explanation", length = 3000)
	private String explanation;
	
	@Lob
    @Column(name = "choice_json", nullable = false)
	private String choiceJson;
	
	
	

	
	
	public void changeText(String questionText) {
		this.questionText = questionText;
	}
	
	public void changeAnswer(char answer) {
		this.answer = answer;
	}
	
	public void changeExplanation(String explanation) {
		this.explanation = explanation;
	}
	
	public void changeJson(String choiceJson) {
		this.choiceJson = choiceJson;
	}
	
}







