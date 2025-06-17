package com.codeit.mini.service.vending;

import com.codeit.mini.dto.vending.TestCouponDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.TestCouponEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;

public interface ITestCouponService {

	TestCouponDTO issueTestCoupon(Long memberId, Long itemId, int totalCnt, Long machineId);
	
    public TestCouponDTO useSelectedCoupon(Long memberId, String couponCode);
    
    public TestCouponDTO useTestCoupon(Long memberId, Long itemId);
    
    public boolean isValidTestCoupon(String couponCode);
    
    default TestCouponDTO toDTO(TestCouponEntity entity) {
    	TestCouponDTO dto = TestCouponDTO.builder()
    									 .memberId(entity.getMemberId().getMemberId())
    									 .itemId(entity.getItemId().getItemId())
    									 .couponCode(entity.getCouponCode())
    									 .totalCnt(entity.getTotalCnt())
    									 .remainCnt(entity.getRemainCnt())
    									 .status(entity.getStatus())
    									 .build();
    	return dto;
    }
    
    default TestCouponEntity toEntity(TestCouponDTO dto, MemberEntity mEntity, VendingItemEntity itemEntity) {
    	TestCouponEntity entity = TestCouponEntity.builder()
												  .memberId(mEntity)
												  .itemId(itemEntity)
												  .couponCode(dto.getCouponCode())
												  .totalCnt(dto.getTotalCnt())
												  .remainCnt(dto.getRemainCnt())
												  .status(dto.getStatus())
												  .build();
    	return entity;
    }
}
