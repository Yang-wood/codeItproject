package com.codeit.mini.service.vending.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.dto.vending.TestCouponDTO;
import com.codeit.mini.entity.comm.CouponStatusEnum;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.TestCouponEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.vending.IMachineItemRepository;
import com.codeit.mini.repository.vending.ITestCouponRepository;
import com.codeit.mini.repository.vending.IVendingItemRepository;
import com.codeit.mini.repository.vending.IVendingMachinesRepository;
import com.codeit.mini.service.vending.ITestCouponHistoryService;
import com.codeit.mini.service.vending.ITestCouponService;
import com.codeit.mini.util.CouponCodeGenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestCouponServiceImpl implements ITestCouponService{

	private final ITestCouponRepository testCouponRepository;
	private final IMemberRepository memberRepository;
	private final IVendingItemRepository itemRepository;
	private final ITestCouponHistoryService couponHistoryService;
	private final IMachineItemRepository machineItemRepository;
	
	@Transactional
	@Override
	public TestCouponDTO issueTestCoupon(Long memberId, Long itemId, int totalCnt, Long machineId) {
		 
		MemberEntity member = memberRepository.findById(memberId)
	            							  .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
		
		VendingItemEntity item = itemRepository.findById(itemId)
		        							   .orElseThrow(() -> new RuntimeException("존재하지 않는 아이템입니다."));
		
		String itemType = item.getItemType().toLowerCase(); // exam, free 등
		boolean isTest = itemType.equals("test");
		boolean isFree = itemType.equals("free");
		
		MachineItemEntity machineItem = machineItemRepository.findMachineItem(machineId, itemId)
															 .orElseThrow(() -> new RuntimeException("해당 자판기와 상품 연결 없음"));
		
		String machineType = machineItem.getVendingMachine().getType().toLowerCase();
	    boolean isRandom = machineType.equals("random");
	    
	    if (!isRandom && isTest) {
	        return testCouponRepository.findByMemberIdAndItemId(member, item)
	            .map(coupon -> {
	                coupon.setTotalCnt(coupon.getTotalCnt() + totalCnt);
	                coupon.setRemainCnt(coupon.getRemainCnt() + totalCnt);
	                TestCouponEntity updated = testCouponRepository.save(coupon);
	                return toDTO(testCouponRepository.save(coupon));
	            })
	            .orElseGet(() -> createCoupon(member, item, totalCnt, isTest || isFree));
	    }
		
		if (isRandom) {
			return createCoupon(member, item, totalCnt, isTest || isFree);
		}
	    
	    boolean alreadyIssued = testCouponRepository.existsByMemberIdAndItemId(member, item);
		if (alreadyIssued) {
			throw new IllegalStateException("이미 해당 아이템에 대해 쿠폰이 발급되었습니다.");
		}

		return createCoupon(member, item, totalCnt, isTest || isFree);
	}
	
	private TestCouponDTO createCoupon(MemberEntity member, VendingItemEntity item, int totalCnt, boolean hasExpireDate) {
	    String couponCode;
	    do {
	        couponCode = CouponCodeGenerator.generateCode();
	    } while (testCouponRepository.existsByCouponCode(couponCode));

	    TestCouponEntity coupon = TestCouponEntity.builder()
												  .memberId(member)
												  .itemId(item)
												  .couponCode(couponCode)
												  .totalCnt(totalCnt)
												  .remainCnt(totalCnt)
												  .status(CouponStatusEnum.ISSUED)
												  .build();

	    if (hasExpireDate) {
	        coupon.setExpireDate(LocalDateTime.now().plusDays(7));
	    }

	    TestCouponEntity saved = testCouponRepository.save(coupon);
	    couponHistoryService.saveHistory(member.getMemberId(), saved.getTestCouponId(), saved.getCouponCode(), "ISSUED");
	    
	    return toDTO(saved);
	}
	
	@Transactional
	@Override
	public Optional<TestCouponDTO> useFreePassIfExists(Long memberId) {
	    Optional<TestCouponEntity> freePassOpt = testCouponRepository.findUsableCouponWithNearestExpireDate(memberId, null, CouponStatusEnum.ISSUED, "free");

	    if (freePassOpt.isEmpty()) return Optional.empty();

	    TestCouponEntity coupon = freePassOpt.get();

	    if (coupon.getRemainCnt() <= 0) {
	        return Optional.empty();
	    }

	    coupon.setRemainCnt(coupon.getRemainCnt() - 1);

	    if (coupon.getRemainCnt() == 0) {
	        coupon.setStatus(CouponStatusEnum.USED);
	    }

	    testCouponRepository.save(coupon);
	    couponHistoryService.saveHistory(memberId, coupon.getTestCouponId(), coupon.getCouponCode(), "USED");

	    return Optional.of(toDTO(coupon));
	}
	
	@Transactional
	@Override
	public TestCouponDTO useSelectedCoupon(Long memberId, String couponCode) {
		TestCouponEntity coupon = testCouponRepository.findByCouponCode(couponCode)
													  .orElseThrow(() -> new RuntimeException("존재하지 않는 쿠폰입니다."));

		if (coupon.getStatus() != CouponStatusEnum.ISSUED) {
		    throw new IllegalStateException("사용할 수 없는 쿠폰입니다.");
		}
			
		if (coupon.getRemainCnt() <= 0) {
		    throw new IllegalStateException("남은 사용 횟수가 없습니다.");
		}
			
		coupon.setRemainCnt(coupon.getRemainCnt() - 1);
			
		if (coupon.getRemainCnt() == 0) {
		    coupon.setStatus(CouponStatusEnum.USED);
		}
			
		testCouponRepository.save(coupon);
		
		couponHistoryService.saveHistory(memberId, coupon.getTestCouponId(), coupon.getCouponCode(), "USED");
		
		return toDTO(coupon);
	}
	
	@Transactional
	@Override
	public TestCouponDTO useTestCoupon(Long memberId, Long itemId) {
	    TestCouponEntity coupon = testCouponRepository.findUsableCouponWithNearestExpireDate(memberId, itemId, CouponStatusEnum.ISSUED)
	    											  .orElseThrow(() -> new RuntimeException("사용 가능한 쿠폰이 없습니다."));

	    if (coupon.getRemainCnt() <= 0) {
	        throw new IllegalStateException("남은 사용 횟수가 없습니다.");
	    }

	    coupon.setRemainCnt(coupon.getRemainCnt() - 1);

	    if (coupon.getRemainCnt() == 0) {
	        coupon.setStatus(CouponStatusEnum.USED);
	    }

	    testCouponRepository.save(coupon);
	    couponHistoryService.saveHistory(memberId, coupon.getTestCouponId(), coupon.getCouponCode(), "USED");

	    return toDTO(coupon);
	}
	
	@Transactional
	@Override
	public boolean isValidTestCoupon(String couponCode) {
        Optional<TestCouponEntity> optional = testCouponRepository.findByCouponCode(couponCode);

        if (optional.isEmpty()) return false;

        TestCouponEntity coupon = optional.get();

        return coupon.getStatus() == CouponStatusEnum.ISSUED && coupon.getRemainCnt() > 0;
	}
	
}