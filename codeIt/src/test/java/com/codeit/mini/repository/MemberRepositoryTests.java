//package com.codeit.mini.repository;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.codeit.mini.entity.member.MemberEntity;
//import com.codeit.mini.repository.member.IMemberRepository;
//
//import lombok.extern.log4j.Log4j2;
//@SpringBootTest
//@Log4j2
//class MemberRepositoryTests {
//
//	@Autowired
//	private IMemberRepository memberRepository;
//	
//	@Test
//	void testInsert() {
//		
//		MemberEntity memberEntity = MemberEntity.builder()
//										.loginId("user1")
//										.memberPw("1234")
//										.memberName("테스트회원")
//										.memberEmail("user1@naver.com")
//										.points(5000)
//										.emailVerified('N')
//										.termsAgreed('Y')
//										.build();
//		memberRepository.save(memberEntity);
//		
//	}
//
//}
