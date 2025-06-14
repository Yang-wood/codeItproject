package com.codeit.mini.repository.omr;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
