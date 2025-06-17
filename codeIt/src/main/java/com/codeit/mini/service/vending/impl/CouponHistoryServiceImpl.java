package com.codeit.mini.service.vending.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codeit.mini.dto.vending.CouponHistoryDTO;
import com.codeit.mini.dto.vending.CouponHistoryRequestDTO;
import com.codeit.mini.dto.vending.CouponStatusDTO;
import com.codeit.mini.entity.comm.CouponStatusEnum;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.CouponHistoryEntity;
import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.vending.ICouponHistoryRepository;
import com.codeit.mini.repository.vending.IMachineItemRepository;
import com.codeit.mini.repository.vending.IVendingItemRepository;
import com.codeit.mini.repository.vending.querydsl.ICouponStatusRepository;
import com.codeit.mini.service.vending.ICouponHistoryService;
import com.codeit.mini.util.CouponCodeGenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class CouponHistoryServiceImpl implements ICouponHistoryService{
	
	private final ICouponHistoryRepository couponRepository;
	private final ICouponStatusRepository couponStatusRepository;
	private final IMemberRepository memberRepository;
	private final IMachineItemRepository machineItemRepository;
	private final IVendingItemRepository itemRepository;

	@Transactional
	@Override
	public CouponHistoryEntity issueCoupon(Long memberId, Long itemId, Long machineId) {
		
		MemberEntity member = memberRepository.findById(memberId)
											  .orElseThrow(() -> new RuntimeException("존재하지 않는 회원"));
		
		VendingItemEntity item = itemRepository.findById(itemId)
											   .orElseThrow(() -> new RuntimeException("존재하지 않는 상품"));
		
		MachineItemEntity machineItem = machineItemRepository.findMachineItem(machineId, itemId)
															 .orElseThrow(() -> new RuntimeException("해당 자판기와 상품 연결 없음"));
		
		VendingMachinesEntity vendingMachine = machineItem.getVendingMachine();
	    String vendingType = vendingMachine.getType().name().toLowerCase();
	    String itemType = item.getItemType().toLowerCase();

	    boolean isRandom = vendingType.equals("random");
	    boolean isRentalOrDiscount = itemType.equals("rental") || itemType.equals("discount");
	    
	    if (!isRandom || !isRentalOrDiscount) {
	        boolean alreadyIssued = couponRepository.existsByMemberId_MemberIdAndItemId_ItemIdAndStatus(
	        										 memberId, itemId, CouponStatusEnum.ISSUED);
	        if (alreadyIssued) {
	            throw new IllegalStateException("이미 유효한 쿠폰이 존재합니다.");
	        }
	    }
	    
	    String couponCode;
	    do {
	        couponCode = CouponCodeGenerator.generateCode();
	    } while (couponRepository.existsByCouponCode(couponCode));
	    
	    LocalDateTime expireDate = LocalDateTime.now().plusDays(7);
		
	    CouponHistoryEntity entity = CouponHistoryEntity.builder()
											            .memberId(member)
											            .itemId(item)
											            .couponCode(couponCode)
											            .couponType(item.getItemType())
											            .status(CouponStatusEnum.ISSUED)
											            .build();
		
		entity.setExpireDate(expireDate);
		return couponRepository.save(entity);
	}

	@Transactional
	@Override
	public CouponHistoryDTO findByCouponCode(String couponCode) {
		
		CouponHistoryEntity entity = couponRepository.findByCouponCode(couponCode)
													 .orElseThrow(() -> new RuntimeException("존재하지 않는 쿠폰 코드 입니다."));
		
		if (!entity.getStatus().isUsable()) {
			throw new IllegalStateException("이미 사용되었거나 유효하지 않은 쿠폰입니다.");
		}
		
		if (entity.getExpireDate() != null && entity.getExpireDate().isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("이미 만료된 쿠폰입니다.");
			
		}
		
		return toDTO(entity);
	}

	@Transactional
	@Override
	public Page<CouponHistoryEntity> getCouponsByMember(Long memberId, Pageable pageable) {
		
	    if (!memberRepository.existsById(memberId)) {
	        throw new RuntimeException("존재하지 않는 회원입니다.");
	    }

		return couponRepository.findByMemberId_MemberId(memberId, pageable);
	}

	@Transactional
	@Override
	public void useCoupon(String couponCode) {
		
		CouponHistoryEntity coupon = couponRepository.findByCouponCode(couponCode)
				 									 .orElseThrow(() -> new RuntimeException("존재하지 않는 쿠폰 코드입니다."));
	if (!coupon.getStatus().isUsable()) {
		throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
	}
	
	if (coupon.getExpireDate() != null && coupon.getExpireDate().isBefore(LocalDateTime.now())) {
		throw new IllegalStateException("이미 만료된 쿠폰입니다.");
	}
		
		coupon.setStatus(CouponStatusEnum.USED);
		coupon.setUsedDate(LocalDateTime.now());
		
		CouponHistoryEntity saved = couponRepository.save(coupon);
		
	}

	@Transactional
	@Override
	public boolean isValidCoupon(String couponCode) {
		
		Optional<CouponHistoryEntity> checkCoupon = couponRepository.findByCouponCode(couponCode);
		
		if (checkCoupon.isEmpty()) {
			return false;
		}
		
		CouponHistoryEntity coupon = checkCoupon.get();
		
		if (!coupon.getStatus().isUsable()) {
			return false;
		}
		
		if (coupon.getExpireDate() != null && coupon.getExpireDate().isBefore(LocalDateTime.now())) {
			return false;
		}
		
		return true;
	}

	@Transactional
	@Override
	public int expireOldCoupons() {
		List<CouponHistoryEntity> expiredList = couponRepository.findAllByStatusAndExpireDateBefore(CouponStatusEnum.ISSUED, LocalDateTime.now());
		
		for (CouponHistoryEntity coupon : expiredList) {
			coupon.setStatus(CouponStatusEnum.EXPIRED);
		}
		
		couponRepository.saveAll(expiredList);
		
		return expiredList.size();
	}
	
	@Override
	public List<CouponStatusDTO> getCouponStats(CouponHistoryRequestDTO request) {
		return couponStatusRepository.getCouponStats(request);
	}
	
	@Override
	public Page<CouponStatusDTO> getCouponStatsPage(CouponHistoryRequestDTO request) {
		return couponStatusRepository.getCouponStatsPage(request);
	}

	@Override
	public CouponStatusDTO getCouponStatsByItem(Long itemId) {
		return couponStatusRepository.getCouponStatsByItem(itemId);
	}

}