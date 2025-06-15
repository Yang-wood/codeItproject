package com.codeit.mini.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeit.mini.entity.admin.Admin;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class AdminRepositoryTest {
	
	@Autowired
	private IAdminRepository adminRepository;

	@Test
	void testInsertAdmin() {
		
		Admin admin = Admin.builder().adminLoginId("admin2")
									 .adminPw("1234")
									 .adminName("관리자2")
									 .adminEmail("admin2@naver.com")
									 .build();
		
		adminRepository.save(admin);
	}

}
