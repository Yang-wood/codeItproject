package com.codeit.mini.repository.omr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.dto.omr.TestSessionSummaryDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.omr.TestSessionEntity;

public interface ITestSessionRepository extends JpaRepository<TestSessionEntity, Long>{
	
	
	@Query("SELECT s "
		 + "FROM TestSessionEntity s "
		 + "WHERE s.memberId = :memberId "
		 + "ORDER BY s.submitTime DESC")
	List<TestSessionEntity> findAllByMemberId(@Param("memberId") MemberEntity memberId);
	
	
	
	@Query("SELECT new com.codeit.mini.dto.omr.TestSessionSummaryDTO "
			+ "(s.sessionId, s.memberId.memberId, s.testId.testId, s.submitTime, s.score, s.duration, c.categoryType) "
		 + "FROM TestSessionEntity s "
		 + "JOIN s.testId t "
		 + "JOIN t.categoryId c "
		 + "WHERE s.memberId.memberId = :memberId "
		 + "ORDER BY s.submitTime DESC")
	List<TestSessionSummaryDTO> findSummaryByMemberId(@Param("memberId") Long memberId);
	
}
