//package com.codeit.mini.component;
//
//import java.io.Reader;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVRecord;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import com.codeit.mini.entity.omr.TestEntity;
//import com.codeit.mini.entity.omr.TestQuestionEntity;
//import com.codeit.mini.repository.omr.ITestQuestionRepository;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//
//@Component
//@RequiredArgsConstructor
//@Log4j2
//public class QuestionCsvImporter implements CommandLineRunner{
//	
//		// TODO 톰캣 실행하기 전에 메이븐 라이브러리 옮기기
//	private final ITestQuestionRepository questionRepository;
//	
//	@Override
//	public void run(String... args) throws Exception {
//		Path csvPath = Paths.get("src/main/resources/csv/question1.csv");
//		
//		
//		try (
//			// 파일을 UTF-8 로 읽는다
//			Reader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8);
//			CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
//		) {
//			
//			List<TestQuestionEntity> questionList = new ArrayList<>();
//			
//			for (CSVRecord record : parser) {
//				try {
//					
//					String questionText = record.get("문제");
//					String rawAnswer = record.get("정답").trim();
//					String explanation = record.get("해설");
//					String choiceJson = record.get("choiceJson").trim();
//					
//					// 정답이 1~4 숫자가 아니면 스킵
//					if (rawAnswer.length() != 1 || !"1234".contains(rawAnswer)) {
//						log.info("잘못된 Answer : {}", rawAnswer);
//						continue;
//					}
//					
//					// 옳바른 정답 변수 선언
//					char answer = rawAnswer.charAt(0);
//					
//					// 추가하려는 문제가 DB에 있을 시 스킵
//					if (questionRepository.existsByQuestionText(questionText)) {
//						continue;
//					}
//					
//					// 문제를 추가하려는 테스트Id 지정
//					TestEntity testRef = TestEntity.builder().testId(1L).build();
//					
//					// DTO를 엔티티화
//					TestQuestionEntity questionEntity = TestQuestionEntity.builder()
//															.testId(testRef)
//															.questionText(questionText)
//															.answer(answer)
//															.explanation(explanation)
//															.choiceJson(choiceJson)
//															.build();
//					// List에 엔티티 주입
//					questionList.add(questionEntity);
//					
//				} catch (Exception e) {
//					log.error("문제 생성 중 예외 발생 {}", e.getMessage());				
//				}
//				
//			}
//			// List를 레파지토리에 저장
//			questionRepository.saveAll(questionList);
//			log.info("총 {} 개 문제 저장 완료", questionList.size());
//		}
//		
//
//	}
//	
//	
//
//}
//
//
//
//
//
//
//
//
