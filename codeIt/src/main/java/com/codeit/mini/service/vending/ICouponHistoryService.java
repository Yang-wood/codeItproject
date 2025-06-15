package com.codeit.mini.service.vending;

import java.util.List;
import java.util.Optional;

import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.dto.vending.CouponHistoryDTO;
import com.codeit.mini.dto.vending.CouponStatusDTO;
import com.codeit.mini.dto.vending.VendingItemDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.CouponHistoryEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;

public interface ICouponHistoryService {

	CouponHistoryEntity issueCoupon(Long memberId, Long itemId, Long machineId);
	
	Optional<CouponHistoryEntity> findByCouponCode(String couponCode);
	
	List<CouponHistoryEntity> getCouponsByMember(Long memberId);
	
	void useCoupon(String couponCode);
	
	boolean isValidCoupon(String couponCode);
	
	int expireOldCoupons();
	
	List<CouponHistoryEntity> getAllCouponHistories();
	
	CouponStatusDTO getCouponStatsByItem(Long itemId);
	
	default CouponHistoryDTO toDTO (CouponHistoryEntity entity) {
		CouponHistoryDTO dto = CouponHistoryDTO.builder()
											   .couponId(entity.getCouponId())
											   .memberId(entity.getMemberId().getMemberId())
											   .itemId(entity.getItemId().getItemId())
											   .couponCode(entity.getCouponCode())
											   .couponType(entity.getCouponType())
											   .status(entity.getStatus())
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
														.status(dto.getStatus())
														.build();
		
		entity.setUsedDate(dto.getUsedDate());
		entity.setExpireDate(dto.getExpireDate());
		
		return entity;
	}
	
}
