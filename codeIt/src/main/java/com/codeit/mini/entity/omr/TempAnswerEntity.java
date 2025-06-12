package com.codeit.mini.entity.omr;

import java.time.LocalDateTime;

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
		name = "TEMP_ANSWER_SEQ_GEN",
		sequenceName = "temp_answer_seq",
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
@Table(name = "temp_answer")
public class TempAnswerEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "TEMP_ANSWER_SEQ_GEN")
    @Column(name = "temp_id")
    private Long tempId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private TestSessionEntity sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private TestQuestionEntity questionId;

    @Column(name = "choice_answer", columnDefinition = "CHAR(1)")
    private char choiceAnswer;

    @Column(name = "save_time", columnDefinition = "DATE DEFAULT SYSDATE")
    private LocalDateTime saveTime;
    
    
 /*   
    public TempAnswerEntity toDto(TempAnswerDTO tempAnswerDto) {
    	
    }
*/
    
}
