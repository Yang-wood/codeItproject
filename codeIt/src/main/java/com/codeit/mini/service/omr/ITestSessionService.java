package com.codeit.mini.service.omr;

import java.util.List;

import com.codeit.mini.dto.omr.TestAnswerDTO;
import com.codeit.mini.dto.omr.TestSessionDTO;
import com.codeit.mini.dto.omr.TestSessionSummaryDTO;
import com.codeit.mini.entity.omr.TestSessionEntity;

public interface ITestSessionService {
	
	Long register(TestSessionDTO dto);
	TestSessionDTO findById(Long sessionId);
	List<TestSessionDTO> findAll();
	void delete(Long sessionId);
	void changeSubmitStatus(Long sessionId, char status);
	
	// 문제 답변 임시 저장
	void saveSessionWithAnswers(List<TestAnswerDTO> answers, Long memberId, Long testId, int score, int durationSec);
	
	// 문제풀이 이력 불러오기
	List<TestSessionDTO> getSessionListByMemberId(Long memberId);
	
	// testId로 categoryType 가져오기
	List<TestSessionSummaryDTO> getSessionSummaryByMemberId(Long memberId);
	
	
	Long startTest(Long memberId, Long testId);
	
	void submitTest(Long sessionId);
	
	
	
	
	default TestSessionDTO entityToDto(TestSessionEntity entity) {
		return TestSessionDTO.builder()
								.sessionId(entity.getSessionId())
								.memberId(entity.getMemberId().getMemberId())
								.testId(entity.getTestId().getTestId())
								.startTime(entity.getStartTime())
								.submitTime(entity.getSubmitTime())
								.score(entity.getScore())
								.duration(entity.getDuration())
								.isSubmited(entity.getIsSubmited())
								.build();
	}
	
	default TestSessionEntity dtoToEntity(TestSessionDTO dto) {
		return TestSessionEntity.builder()
									.sessionId(dto.getSessionId())
									.startTime(dto.getStartTime())
									.submitTime(dto.getSubmitTime())
									.score(dto.getScore())
									.duration(dto.getDuration())
									.isSubmited(dto.getIsSubmited())
									.build();
	}
}
