package com.codeit.mini.service.vending;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.vending.VendingHistoryDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.CouponHistoryEntity;
import com.codeit.mini.entity.vending.PointHistoryEntity;
import com.codeit.mini.entity.vending.VendingHistoryEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;

public interface IVendingHistoryService {

	PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> getAllHistories(PageRequestDTO requestDTO);
	
	PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> getHistoriesByMember(Long memberId, PageRequestDTO requestDTO);
	
	PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> getHistoriesByMachine(Long machineId, PageRequestDTO requestDTO);
	
	long countByMember(Long memberId);
	
	long countByMachine(Long machineId);
		
	int getTotalUsedPoints(Long memberId);
		
	int getTotalRewardedPoints(Long memberId);
		
	public PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> searchHistories(Long memberId, Long machineId, String payment, String status, PageRequestDTO requestDTO);

	
	default VendingHistoryDTO toDTO (VendingHistoryEntity entity) {
		VendingHistoryDTO dto = VendingHistoryDTO.builder()
												 .memberId(entity.getMemberId().getMemberId())
												 .itemId(entity.getItemId().getItemId())
												 .payment(entity.getPayment())
												 .status(entity.getStatus())
												 .pointId(entity.getPointId().getPointId())
												 .couponId(entity.getCouponId().getCouponId())
												 .build();
		
		return dto;
	}
	
	default VendingHistoryEntity toEntity (VendingHistoryDTO dto, MemberEntity mEntity, VendingItemEntity itemEntity, PointHistoryEntity pointEntity, CouponHistoryEntity couponEntity) {
		VendingHistoryEntity entity = VendingHistoryEntity.builder()
														  .memberId(mEntity)
														  .itemId(itemEntity)
														  .payment(dto.getPayment())
														  .status(dto.getStatus())
														  .pointId(pointEntity)
														  .couponId(couponEntity)
														  .build();
		
		return entity;
	}
}
