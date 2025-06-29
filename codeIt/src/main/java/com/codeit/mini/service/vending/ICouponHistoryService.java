package com.codeit.mini.service.vending;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.comm.UnifiedCouponHistoryDTO;
import com.codeit.mini.dto.vending.CouponHistoryDTO;
import com.codeit.mini.dto.vending.CouponHistoryRequestDTO;
import com.codeit.mini.dto.vending.CouponStatusDTO;
import com.codeit.mini.entity.comm.CouponStatusEnum;
import com.codeit.mini.entity.comm.VendingType;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.CouponHistoryEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;

public interface ICouponHistoryService {

	CouponHistoryEntity issueCoupon(Long memberId, Long itemId, Long machineId);
	
	CouponHistoryDTO findByCouponCode(String couponCode);
	
	Page<CouponHistoryEntity> getCouponsByMember(Long memberId, Pageable pageable);
	
	void useCoupon(String couponCode);
	
	boolean isValidCoupon(String couponCode);
	
	int expireOldCoupons();
	
	List<CouponStatusDTO> getCouponStats(CouponHistoryRequestDTO request);
	
	Page<CouponStatusDTO> getCouponStatsPage(CouponHistoryRequestDTO request);
	
	CouponStatusDTO getCouponStatsByItem(Long itemId);
	
	Page<CouponHistoryEntity> getCouponHistoryByItem(Long itemId, Pageable pageable);
	
	public PageResultDTO<UnifiedCouponHistoryDTO, UnifiedCouponHistoryDTO> getAllUserCouponsPaged(Long memberId, PageRequestDTO requestDTO);
	
	default CouponHistoryDTO toDTO (CouponHistoryEntity entity) {
		CouponHistoryDTO dto = CouponHistoryDTO.builder()
											   .couponId(entity.getCouponId())
											   .memberId(entity.getMemberId().getMemberId())
											   .itemId(entity.getItemId().getItemId())
											   .couponCode(entity.getCouponCode())
											   .couponType(entity.getCouponType())
											   .status(entity.getStatus().name())
											   .discountRate(entity.getDiscountRate())
											   .issuedDate(entity.getIssuedDate())
											   .usedDate(entity.getUsedDate())
											   .expireDate(entity.getExpireDate())
											   .build();
		
		return dto;
	}
	
	default CouponHistoryEntity toEntity (CouponHistoryDTO dto, MemberEntity mEntity, VendingItemEntity itemEntity) {
		CouponHistoryEntity entity = CouponHistoryEntity.builder()
														.couponId(dto.getCouponId())
														.memberId(mEntity)
														.itemId(itemEntity)
														.couponCode(dto.getCouponCode())
														.couponType(dto.getCouponType())
														.discountRate(dto.getDiscountRate())
														.status(CouponStatusEnum.valueOf(dto.getStatus()))
														.build();
		
		entity.setUsedDate(dto.getUsedDate());
		entity.setExpireDate(dto.getExpireDate());
		
		return entity;
	}
	
}
