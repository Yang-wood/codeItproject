package com.codeit.mini.entity.omr;

import java.sql.Timestamp;

import org.hibernate.annotations.Check;

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

@Entity
@SequenceGenerator(
		name = "TEST_SESSION_SEQ_GEN",
		sequenceName = "test_session_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Check(constraints = "is_submited IN ('Y', 'N')")
@Table(name = "test_session")
public class TestSessionEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    				generator = "TEST_SESSION_SEQ_GEN")
    @Column(name = "session_id")
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private TestEntity testId;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "submit_time")
    private Timestamp submitTime;

    @Column(name = "score")
    private Integer score;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "is_submited", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private char isSubmited = 'N';
    
    
    
    public void changeSubmit(char isSubmited) {
    	this.isSubmited = isSubmited;
    }
    
    
    
	
}
