package com.codeit.mini.repository.omr;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.entity.omr.TestEntity;
import com.codeit.mini.entity.vending.TestCouponEntity;

public interface ITestRepository extends JpaRepository<TestEntity, Long>{
	
	
	Page<TestEntity> findByIsOpen(char isOpen, Pageable pageable);
	
	Page<TestEntity> findByCategoryId_CategoryTypeAndIsOpen(String categoryType, char isOpen, Pageable pageable);
	
	Page<TestEntity> findByCategoryId_CategoryType(String categoryType, Pageable pageable);
	
	@Query("SELECT t FROM TestEntity t WHERE t.isOpen = 'Y'")
	List<TestEntity> findOpenTest();
	
	

	
	
}
