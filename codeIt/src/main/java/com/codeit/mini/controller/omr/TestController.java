package com.codeit.mini.controller.omr;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codeit.mini.dto.AdminDTO;
import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.dto.omr.TestAnswerDetailDTO;
import com.codeit.mini.dto.omr.TestSessionDTO;
import com.codeit.mini.dto.omr.TestSessionSummaryDTO;
import com.codeit.mini.service.omr.ITestAnswerService;
import com.codeit.mini.service.omr.ITestSessionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/omr")
@RequiredArgsConstructor
public class TestController {
	
	private final ITestSessionService sessionService;
	private final ITestAnswerService answerService;
	
	
	@GetMapping("/test")
	public String testPage() {
		
		return "/omr/test";
	}
	
	
	@GetMapping("/history")
	public String getHistoryPage(HttpSession session, Model model) {
		MemberDTO memberDto = (MemberDTO)session.getAttribute("member");
		AdminDTO adminDto = (AdminDTO)session.getAttribute("admin");
		
		Long memberId = null;

	    if (memberDto != null) {
	        memberId = memberDto.getMemberId();
	        
	    } else if (adminDto != null) {
	        memberId = adminDto.getAdminId();
	        
	    } else {
	        return "redirect:/member/login";
	    }

	    List<TestSessionSummaryDTO> sessionList = sessionService.getSessionSummaryByMemberId(memberId);
	    model.addAttribute("sessionList", sessionList);
		
		return "/omr/history";
	}
	
	
	@GetMapping("/result/{sessionId}")
	public String getResultDetail(@PathVariable("sessionId") Long sessionId, Model model) {
		List<TestAnswerDetailDTO> answer = answerService.getAnswerDetail(sessionId);
		
		model.addAttribute("answerList", answer);
		
		return "/omr/result";
		
	}
	
	

	@GetMapping("/exam")
	public String examPage() {
		
		return "/omr/exam";
	}
	
	
	
	
	
	
	
	
}
