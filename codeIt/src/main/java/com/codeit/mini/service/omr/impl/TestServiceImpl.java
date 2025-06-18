package com.codeit.mini.service.omr.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.codeit.mini.dto.omr.TestDTO;
import com.codeit.mini.dto.vending.TestCouponDTO;
import com.codeit.mini.entity.comm.CouponStatusEnum;
import com.codeit.mini.entity.omr.CategoryEntity;
import com.codeit.mini.entity.omr.TestEntity;
import com.codeit.mini.entity.vending.TestCouponEntity;
import com.codeit.mini.entity.vending.TestCouponHistoryEntity;
import com.codeit.mini.repository.omr.ITestQuestionRepository;
import com.codeit.mini.repository.omr.ITestRepository;
import com.codeit.mini.repository.omr.ITestSessionRepository;
import com.codeit.mini.repository.vending.ITestCouponHistoryRepository;
import com.codeit.mini.repository.vending.ITestCouponRepository;
import com.codeit.mini.service.omr.ITestService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class TestServiceImpl implements ITestService{
	
	private final ITestRepository testRepository;
	private final ITestQuestionRepository questionRepository;
	private final ITestSessionRepository sessionRepository;
	private final ITestCouponRepository testCouponRepository;
	private final ITestCouponHistoryRepository testCouponHistoryRepository;

	@Override
	public Long register(TestDTO dto) {
		TestEntity entity = dtoToEntity(dto);
		
		testRepository.save(entity);
		
		return entity.getTestId();
	}

	@Override
	public TestDTO findById(Long testId) {
		return testRepository.findById(testId).map(this::entityToDto)
											  .orElse(null);
	}

	@Override
	public List<TestDTO> getList() {
		return testRepository.findAll().stream().map(this::entityToDto)
												.collect(Collectors.toList());
	}

	@Override
	public void update(TestDTO dto) {
		TestEntity entity = testRepository.findById(dto.getTestId()).orElseThrow();
		
		entity.changeTitle(dto.getTestTitle());
		entity.changeDesc(dto.getTestDesc());
		entity.changeIsOpen(dto.getIsOpen());
		
		testRepository.save(entity);
	}

	@Override
	public void delete(Long testId) {
		testRepository.deleteById(testId);
	}

	@Override
	public Page<TestDTO> getPageList(String category, int page) {
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("testId").descending());
		
		
		Page<TestEntity> result;
		
		if ("all".equalsIgnoreCase(category)) {
			result = testRepository.findAll(pageable);
		} else {
			result = testRepository.findByCategoryId_CategoryType(category, pageable);
		}
		
		return result.map(this::entityToDto);
	}

	@Override
	public Page<TestDTO> getAllPageList(int page) {
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("testId").descending());
		
		return testRepository.findAll(pageable).map(this::entityToDto);
	}

	@Override
	public void hideTest(Long testId) {
		TestEntity test = testRepository.findById(testId).orElseThrow();
		
		test.changeIsOpen('N');
		
		testRepository.save(test);
	}

	@Override
	public Page<TestDTO> getPublicPageList(String category, int page) {
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("testId").descending());
		
		Page<TestEntity> result;
		
		if ("all".equalsIgnoreCase(category)) {
			result = testRepository.findByIsOpen('Y', pageable);
		} else {
			result = testRepository.findByCategoryId_CategoryTypeAndIsOpen(category, 'Y', pageable);
		}
		
		return result.map(this::entityToDto);
	}



	
	
	
	
//	@Override
//	public boolean checkCoupon(Long memberId) {
//		Boolean result = couponRepository.existsCoupon(memberId, "ISSUED");
//		log.info("쿠폰 수량 체크 : memberId = {}, status = {}, 결과 = {}", memberId, "ISSUED", result);
//		
//		CouponStatusEnum status = CouponStatusEnum.valueOf("ISSUED");
//		log.info("Enum value: " + status);
//		
//		return result != null && result;
//	}
//	
//	
//	@Transactional
//	@Override
//	public void useCoupon(Long memberId) {
//		TestCouponEntity coupon = couponRepository.findOneCoupon(memberId, "ISSUED")
//								.orElseThrow(() -> new IllegalStateException("사용 가능한 쿠폰이 존재하지 않습니다."));
//		log.info("쿠폰 정보 : {}", coupon);
//		
//		
//		int remain = coupon.getRemainCnt();
//		
//		if (remain <= 0) {
//			throw new IllegalStateException("쿠폰 잔여 수량이 없습니다");
//		}
//		
//		coupon.setRemainCnt(remain - 1);
//		couponRepository.save(coupon);
//		
//		TestCouponHistoryEntity history = TestCouponHistoryEntity .builder()
//											.testCouponId(coupon)
//											.memberId(coupon.getMemberId())
//											.couponCode(coupon.getCouponCode())
//											.status("used")
//											.build();
//		couponHistoryRepository.save(history);
//				
//	}

	
	

}
