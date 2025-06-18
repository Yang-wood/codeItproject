package com.codeit.mini.service.omr.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.omr.TestAnswerDTO;
import com.codeit.mini.dto.omr.TestSessionDTO;
import com.codeit.mini.dto.omr.TestSessionSummaryDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.omr.TestAnswerEntity;
import com.codeit.mini.entity.omr.TestEntity;
import com.codeit.mini.entity.omr.TestQuestionEntity;
import com.codeit.mini.entity.omr.TestSessionEntity;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.omr.ITestAnswerRepository;
import com.codeit.mini.repository.omr.ITestQuestionRepository;
import com.codeit.mini.repository.omr.ITestRepository;
import com.codeit.mini.repository.omr.ITestSessionRepository;
import com.codeit.mini.service.omr.ITestSessionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class TestSessionServiceImpl implements ITestSessionService{
	
	private final ITestSessionRepository sessionRepository;
	private final IMemberRepository memberRepository;
	private final ITestRepository testRepository;
	private final ITestQuestionRepository questionRepository;
	private final ITestAnswerRepository answerRepository;
	
	
	@Override
	public Long register(TestSessionDTO dto) {
		MemberEntity member = memberRepository.findById(dto.getMemberId()).get();
		TestEntity test = testRepository.findById(dto.getTestId()).get();
		
		TestSessionEntity entity = dtoToEntity(dto);
		
		entity.setMemberId(member);
		entity.setTestId(test);
		
		TestSessionEntity saved = sessionRepository.save(entity);
		log.info("등록된 세션 : {}", saved);
		
		return saved.getSessionId();
	}

	@Override
	public TestSessionDTO findById(Long sessionId) {
		Optional<TestSessionEntity> result = sessionRepository.findById(sessionId);
		
		return result.map(this::entityToDto).orElse(null);
	}

	@Override
	public List<TestSessionDTO> findAll() {
		return sessionRepository.findAll().stream().map(this::entityToDto)
												   .collect(Collectors.toList());
	}

	@Override
	public void delete(Long sessionId) {
		if (!sessionRepository.existsById(sessionId)) {
			throw new IllegalArgumentException("존재하지 않는 세션입니다");
		}
		
		sessionRepository.deleteById(sessionId);
	}

	@Override
	public void changeSubmitStatus(Long sessionId, char status) {
		TestSessionEntity entity = sessionRepository.findById(sessionId).get();
		
		entity.changeSubmit(status);
		
		sessionRepository.save(entity);
		log.info("변경된 세션={}, 상태={}", sessionId, status);
	}

	@Override
	public void saveSessionWithAnswers(List<TestAnswerDTO> answers, Long memberId, Long testId, int score, int durationSec) {
		MemberEntity member = memberRepository.findById(memberId).orElseThrow();
		TestEntity test = testRepository.findById(testId).orElseThrow();
		
		TestSessionEntity session = TestSessionEntity.builder()
												.memberId(member)
												.testId(test)
												.score(score)
												.duration(durationSec)
												.isSubmited('Y')
												.startTime(new Timestamp(System.currentTimeMillis()))
												.submitTime(new Timestamp(System.currentTimeMillis()))
												.build();
		sessionRepository.save(session);
		
		for (TestAnswerDTO testAnswerDTO : answers) {
			if (testAnswerDTO.getChoiceAnswer() == null) {
				continue;
			}
			
			TestQuestionEntity question = questionRepository.findById(testAnswerDTO.getQuestionId()).orElseThrow();
			
			TestAnswerEntity answer = TestAnswerEntity.builder()
												.sessionId(session)
												.questionId(question)
												.choiceAnswer(testAnswerDTO.getChoiceAnswer())
												.build();
			answerRepository.save(answer);
		}
	}

	@Override
	public List<TestSessionDTO> getSessionListByMemberId(Long memberId) {
		MemberEntity member = memberRepository.findById(memberId).orElseThrow();
		
		List<TestSessionEntity> session = sessionRepository.findAllByMemberId(member);
		
		return session.stream().map(this::entityToDto).collect(Collectors.toList());
	}

	@Override
	public List<TestSessionSummaryDTO> getSessionSummaryByMemberId(Long memberId) {
		return sessionRepository.findSummaryByMemberId(memberId);
	}

	@Override
	public Long startTest(Long memberId, Long testId) {
		MemberEntity member = memberRepository.findById(memberId).orElseThrow();
		TestEntity test = testRepository.findById(testId).orElseThrow();
		
		Timestamp startTime =  new Timestamp(System.currentTimeMillis());
		
		TestSessionEntity session = TestSessionEntity.builder()
											.memberId(member)
											.testId(test)
											.startTime(startTime)
											.isSubmited('N')
											.build();
		TestSessionEntity saved = sessionRepository.save(session);
		
		return saved.getSessionId();
	}
	
	
	@Override
	@Transactional
	public void submitTest(Long sessionId) {
		TestSessionEntity session = sessionRepository.findById(sessionId).orElseThrow();
		
		Timestamp submitTime = new Timestamp(System.currentTimeMillis());
		
		session.setSubmitTime(submitTime);
		
		if (session.getStartTime() != null) {
			long durationMillis = submitTime.getTime() - session.getStartTime().getTime();
			int durationSec = (int)(durationMillis / 1000);
			
			session.setDuration(durationSec);
		} else {
			session.setDuration(0);
		}
		
		session.setIsSubmited('Y');
		
		sessionRepository.save(session);
	}

}










