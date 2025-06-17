package com.codeit.mini.service.vending;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.vending.TestCouponHistoryDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.TestCouponEntity;
import com.codeit.mini.entity.vending.TestCouponHistoryEntity;

public interface ITestCouponHistoryService {

	TestCouponHistoryDTO saveHistory(Long memberId, Long couponId, String couponCode, String status);

    PageResultDTO<TestCouponHistoryDTO, TestCouponHistoryEntity> getHistoriesByMember(Long memberId, PageRequestDTO requestDTO);

    PageResultDTO<TestCouponHistoryDTO, TestCouponHistoryEntity> getHistoriesByCouponCode(String couponCode, PageRequestDTO requestDTO);

    default TestCouponHistoryDTO toDTO(TestCouponHistoryEntity entity) {
        TestCouponHistoryDTO dto = TestCouponHistoryDTO.builder()
													   .historyId(entity.getHistoryId())
													   .memberId(entity.getMemberId().getMemberId())
													   .couponId(entity.getTestCouponId().getTestCouponId())
													   .couponCode(entity.getCouponCode())
													   .status(entity.getStatus())
													   .regDate(entity.getRegDate())
													   .usedDate(entity.getUsedDate())
													   .build();
        return dto;
    }
    
    default TestCouponHistoryEntity toEntity(TestCouponHistoryDTO dto, TestCouponEntity testEntity, MemberEntity mEntity) {
    	TestCouponHistoryEntity entity = TestCouponHistoryEntity.builder()
																.historyId(dto.getHistoryId())
																.memberId(mEntity)
																.testCouponId(testEntity)
																.couponCode(testEntity.getCouponCode())
																.status(dto.getStatus())
																.usedDate(dto.getUsedDate())
																.build();
    	return entity;
    }
}
