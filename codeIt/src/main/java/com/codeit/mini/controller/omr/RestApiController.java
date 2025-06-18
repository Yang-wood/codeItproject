package com.codeit.mini.controller.omr;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.dto.omr.StartTestDTO;
import com.codeit.mini.dto.omr.SubmitTestDTO;
import com.codeit.mini.dto.omr.TestAnswerDTO;
import com.codeit.mini.dto.omr.TestDTO;
import com.codeit.mini.dto.omr.TestQuestionDTO;
import com.codeit.mini.dto.omr.TestSessionDTO;
import com.codeit.mini.dto.vending.TestCouponDTO;
import com.codeit.mini.entity.omr.TestSessionEntity;
import com.codeit.mini.service.omr.ITestQuestionService;
import com.codeit.mini.service.omr.ITestService;
import com.codeit.mini.service.omr.ITestSessionService;
import com.codeit.mini.service.vending.ITestCouponService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class RestApiController {
	
	private final ITestService testService;
	private final ITestQuestionService questionService;
	private final ITestSessionService sessionService;
	private final ITestCouponService testCouponService;
	
	
	@GetMapping("/test/")
	public Page<TestDTO> getTestList(@RequestParam(name = "category", defaultValue = "all") String category,
									 @RequestParam(name = "page", defaultValue = "1") int page,
									 HttpSession session) {
		MemberDTO memberDto = (MemberDTO)session.getAttribute("member");
		Object adminObj = session.getAttribute("admin");
		
		boolean isAdmin = adminObj != null;
		
		if (isAdmin) {
			if ("all".equalsIgnoreCase(category)) {
				return testService.getAllPageList(page);
			}else {
				return testService.getPageList(category, page);
			}
		} else {
			return testService.getPublicPageList(category, page);
		}
	}
	
	
	@GetMapping("/exam/start/{testId}")
	public ResponseEntity<List<TestQuestionDTO>> startExam(@PathVariable("testId") Long testId) {
		List<TestQuestionDTO> questionList = questionService.getRandomQuestions(testId);
		
		return ResponseEntity.ok(questionList);
	}
	
	
	
	
	@GetMapping("/exam/session")
	public ResponseEntity<List<TestSessionDTO>> getSessionList(HttpSession session) {
		MemberDTO memberDto = (MemberDTO)session.getAttribute("member");
		
		if (memberDto == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		List<TestSessionDTO> sessionDto = sessionService.getSessionListByMemberId(memberDto.getMemberId());
		
		return ResponseEntity.ok(sessionDto);
	}
	
	
	@PostMapping("/exam/start")
	public ResponseEntity<Object> startTest(@RequestBody StartTestDTO dto, HttpSession session) {
		Long memberId = dto.getMemberId();
		Long testId = dto.getTestId();
		
		List<TestCouponDTO> couponList = (List<TestCouponDTO>)session.getAttribute("testCoupons");
		
		log.info("시험 시작 요청 : memberId = {}, testId = {}, couponCode = {}, itemId = {}",
				dto.getMemberId(), dto.getTestId(), dto.getCouponCode(), dto.getItemId());
		
		
		// 사용가능 쿠폰 조회 후 갱신
		try {
			TestCouponDTO usedCoupon = null;
			
			if (dto.getCouponCode() != null && !dto.getCouponCode().isBlank()) {
				usedCoupon = testCouponService.useSelectedCoupon(memberId, dto.getCouponCode());
			} else if (dto.getItemId() != null) {
				usedCoupon = testCouponService.useTestCoupon(memberId, dto.getItemId());
			} else {
				usedCoupon = couponList.stream()
									.filter(c -> c.getRemainCnt() != null && c.getRemainCnt() > 0)
									.findFirst()
									.map(c -> testCouponService.useSelectedCoupon(memberId, c.getCouponCode()))
									.orElseThrow(() -> new IllegalArgumentException("사용 가능한 쿠폰이 없습니다"));
			}
			
			// 세션 쿠폰 값 갱신
			if (couponList != null && usedCoupon != null) {
				for (TestCouponDTO testCouponDTO : couponList) {
					if (testCouponDTO.getCouponCode().equals(usedCoupon.getCouponCode())) {
						
						testCouponDTO.setRemainCnt(usedCoupon.getRemainCnt());
						testCouponDTO.setStatus(usedCoupon.getStatus());
					}
				}
				
				log.info("새로 가져온 쿠폰 정보 : {}", couponList);
				session.setAttribute("testCoupons", couponList);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("쿠폰 오류 : " + e.getMessage());
		}
		
		Long sessionId = sessionService.startTest(memberId, testId);
		
		return ResponseEntity.ok(sessionId);
		
	}
	
	
	
	@PostMapping("/exam/submit")
	public ResponseEntity<String> submitAnswer(@RequestBody SubmitTestDTO submitDto,
											   HttpSession session) {
		MemberDTO member = (MemberDTO)session.getAttribute("member");
		
		if (member == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}
		
		List<TestAnswerDTO> answer = submitDto.getAnswers();
		int duration = submitDto.getDuration();
		
		int score = 0;
		
		for (TestAnswerDTO answerDto : answer) {
			log.info("수신한 답안 : {}", answerDto);
			
			Integer correct = questionService.getCorrectAnswerFromDB(answerDto.getQuestionId());
			
			if (answerDto.getChoiceAnswer() != null
					&& Character.getNumericValue(answerDto.getChoiceAnswer()) == correct) {
				score++;
			}
			
		}
		int finalScore = score * 10;
		
		Long testId = questionService.getTestIdByQuestion(answer.get(0).getQuestionId());
		
		sessionService.saveSessionWithAnswers(answer, member.getMemberId(), testId, finalScore, duration);
		
		return ResponseEntity.ok("최종 점수 : " + finalScore + "점");
	}
	
	
	
	@PostMapping("/toggle")
	@ResponseBody
	public ResponseEntity<String> toggleVisibility(@RequestBody Map<String, Long> payload) {
		Long testId = payload.get("testId");
		TestDTO testDto = testService.findById(testId);
		
		
		char status = testDto.getIsOpen() == 'Y' ? 'N' : 'Y';
		testDto.setIsOpen(status);
		
		testService.update(testDto);
		
		return ResponseEntity.ok("완료되셨습니다");
	}
	
	
}







