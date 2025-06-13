package com.codeit.mini.service.omr;

import java.util.List;

import com.codeit.mini.dto.omr.TestDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.omr.CategoryEntity;
import com.codeit.mini.entity.omr.TestEntity;

public interface ITestService {
	
	Long register(TestDTO dto);
	TestDTO findById(Long testId);
	List<TestDTO> getList();
	void update(TestDTO dto);
	void delete(Long testId);
	
	
	
	default TestEntity dtoToEntity(TestDTO dto) {
		return TestEntity.builder()
			.testId(dto.getTestId())
			.categoryId(CategoryEntity.builder().categoryId(dto.getCategoryId()).build())
			.adminId(Admin.builder().adminId(dto.getAdminId()).build())
			.testTitle(dto.getTestTitle())
			.testDesc(dto.getTestDesc())
			.testLimit(dto.getTestLimit())
			.testPaid(dto.getTestPaid())
			.isOpen(dto.getIsOpen())
			.viewCnt(dto.getViewCnt())
			.build();
	}
	
	default TestDTO entityToDto(TestEntity entity) {
		return TestDTO.builder()
			.testId(entity.getTestId())
			.categoryId(entity.getCategoryId().getCategoryId())
			.adminId(entity.getAdminId().getAdminId())
			.testTitle(entity.getTestTitle())
			.testDesc(entity.getTestDesc())
			.testLimit(entity.getTestLimit())
			.testPaid(entity.getTestPaid())
			.isOpen(entity.getIsOpen())
			.viewCnt(entity.getViewCnt())
			.build();
	}
    
    
    
}






