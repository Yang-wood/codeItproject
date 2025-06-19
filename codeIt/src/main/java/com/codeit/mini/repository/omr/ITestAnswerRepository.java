package com.codeit.mini.repository.omr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.dto.omr.TestAnswerDetailDTO;
import com.codeit.mini.entity.omr.TestAnswerEntity;

public interface ITestAnswerRepository extends JpaRepository<TestAnswerEntity, Long>{
	
	List<TestAnswerEntity> findBySessionId_SessionId(Long sessionId);
	List<TestAnswerEntity> findByQuestionId_QuestionId(Long questionId);
	
	
	
	@Query("SELECT new com.codeit.mini.dto.omr.TestAnswerDetailDTO "
			+ "(a.questionId.questionId, q.questionText, a.choiceAnswer, q.choiceJson, q.explanation) "
		 + "FROM TestAnswerEntity a "
		 + "JOIN a.questionId q "
		 + "WHERE a.sessionId.sessionId = :sessionId")
	List<TestAnswerDetailDTO> findAnswerDetailBySessionId(@Param("sessionId") Long sessionId);
}
