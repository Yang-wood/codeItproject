package com.codeit.mini.repository.omr;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.entity.omr.TestQuestionEntity;

public interface ITestQuestionRepository extends JpaRepository<TestQuestionEntity, Long>{
	
	// 추가하려는 문제가 DB에 중복이 있는지 검사 메소드
	boolean existsByQuestionText(String questionText);
	
	// testId 로 그 테스트와 연관된 전체 문제 목록 불러오는 메소드
	@EntityGraph(attributePaths = {"testId"})
	@Query("SELECT q FROM TestQuestionEntity q WHERE q.testId.testId = :testId")
	List<TestQuestionEntity> findByTestId(@Param("testId") Long testId);
	
	
	
	// 문제ID로 시험ID 가져오기
    @Query("SELECT q.testId.testId FROM TestQuestionEntity q WHERE q.questionId = :questionId")
    Long findTestIdByQuestionId(@Param("questionId") Long questionid);
    
    
    // 시험지 삭제 시 문제까지 지우기
    @Modifying
    @Query("DELETE FROM TestQuestionEntity q "
    	 + "WHERE q.testId.testId = :testId")
    void deleteByTestId(@Param("testId") Long testId);
	
}
