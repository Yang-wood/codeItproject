package com.codeit.mini.controller.omr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.codeit.mini.dto.AdminDTO;
import com.codeit.mini.dto.omr.TestDTO;
import com.codeit.mini.dto.omr.TestQuestionDTO;
import com.codeit.mini.service.admin.IAdminService;
import com.codeit.mini.service.omr.ITestQuestionService;
import com.codeit.mini.service.omr.ITestService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("/omr/admin")
@Log4j2
public class AdminTestController {
	
	private final ITestService testService;
	private final ITestQuestionService questionService;
	private final IAdminService adminService;
	
	
	@GetMapping("/register")
	public String getRegister() {
		
		return "/omr/admin/register";
	}
	
	
	@PostMapping("/register")
	public String registerTest(@ModelAttribute TestDTO testDto,
							   @RequestParam("questionCsv") MultipartFile questionCsv,
							   HttpSession session, Model model) {
		
		AdminDTO loginAdmin = (AdminDTO)session.getAttribute("admin");
		
		log.info("어드민 정보 : {}", loginAdmin);
		try {
			testDto.setAdminId(loginAdmin.getAdminId());
			
			Long testId = testService.register(testDto);
			log.info("시험 등록 완료, testId : {}", testId);
			
//			if (!questionCsv.isEmpty()) {
//				importQuestionFromCsv(questionCsv, testId);
//			}
			
			return "redirect:/omr/test";
		} catch (Exception e) {
			log.error("시험 등록 실패 : {}", e.getMessage());
			
			model.addAttribute("errMsg", "등록 실패 했습니다");
			
			return "/omr/admin/register";
		}
	}
	
	
	
	private void importQuestionFromCsv(MultipartFile csvFile, Long testId) throws Exception {
		try (
			Reader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), 
																	 StandardCharsets.UTF_8));
			CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
		) {
			for (CSVRecord csvRecord : parser) {
				String questionText = csvRecord.get("문제").trim();
				String rawAnswer = csvRecord.get("정답").trim();
				String explanation = csvRecord.get("해설").trim();
				String choiceJson = csvRecord.get("choiceJson").trim();
				
				if (rawAnswer.length() != 1 || !"1234".contains(rawAnswer)) {
					log.warn("잘못된 정답 : {}", rawAnswer);
					
					continue;
				}
				
				char answer = rawAnswer.charAt(0);
				
				TestQuestionDTO questionDto = TestQuestionDTO.builder()
												.testId(testId)
												.questionText(questionText)
												.answer(answer)
												.explanation(explanation)
												.choiceJson(choiceJson)
												.build();
				questionService.register(questionDto);
			}
		}
	}
	
	
	@GetMapping("/modify")
	public String getModify(@RequestParam("testId") Long testId, Model model) {
		TestDTO testDto = testService.findById(testId);
		
		model.addAttribute("testDto", testDto);
		
		return "/omr/admin/modify";
		
	}
	
	@PostMapping("/modify")
	public String postModify(@ModelAttribute TestDTO testDto) {
		testService.update(testDto);
		
		return "redirect:/omr/test";
	}
	
/*	
	@PostMapping("/delete")
	@ResponseBody
	public ResponseEntity<String> deleteTest(@RequestBody Map<String, Long> payload) {
		log.info("삭제 요청 : {}", payload);
		Long testId = payload.get("testId");
		
		if (testId == null) {
			log.info("testId 값이 없습니다");
		}
		
		testService.delete(testId);
		
		return ResponseEntity.ok("삭제 성공");
	}
*/	
	
	
	@PostMapping("/hide")
	@ResponseBody
	public ResponseEntity<String> hideTest(@RequestBody Map<String, Long> payload) {
		Long testId = payload.get("testId");
		
		try {
			testService.hideTest(testId);
			
			return ResponseEntity.ok("비공개 처리되었습니다");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("해당 시험이 존재하지 않습니다");
		}
		
		
	}
	
	
	
	
	
}









