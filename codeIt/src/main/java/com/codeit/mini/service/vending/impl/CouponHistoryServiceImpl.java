package com.codeit.mini.service.vending.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.vending.CouponStatusDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.CouponHistoryEntity;
import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.vending.ICouponHistoryRepository;
import com.codeit.mini.repository.vending.IMachineItemRepository;
import com.codeit.mini.repository.vending.IVendingItemRepository;
import com.codeit.mini.service.vending.ICouponHistoryService;
import com.codeit.mini.util.CouponCodeGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class CouponHistoryServiceImpl implements ICouponHistoryService{
	
	private final ICouponHistoryRepository couponRepository;
	private final IMemberRepository memberRepository;
	private final IMachineItemRepository machineItemRepository;
	private final IVendingItemRepository itemRepository;

	@Override
	public CouponHistoryEntity issueCoupon(Long memberId, Long itemId, Long machineId) {
		
		MemberEntity member = memberRepository.findById(memberId)
											  .orElseThrow(() -> new RuntimeException("존재하지 않는 회원"));
		
		VendingItemEntity item = itemRepository.findById(itemId)
											   .orElseThrow(() -> new RuntimeException("존재하지 않는 상품"));
		
		MachineItemEntity machineItem = machineItemRepository.findMachineItem(itemId, machineId)
															 .orElseThrow(() -> new RuntimeException("해당 자판기와 상품 연결 없음"));;
		
		VendingMachinesEntity vendingMachine = machineItem.getVendingMachine();
		String vendingType = vendingMachine.getType();
		
		LocalDateTime expireDate = null;
		
	    if ("RANDOM".equalsIgnoreCase(vendingType)) {
	        expireDate = LocalDateTime.now().plusDays(7);
	        
	    }
		
	    String couponCode = CouponCodeGenerator.generateCode();
		
		CouponHistoryEntity entity = CouponHistoryEntity.builder()
														.memberId(member)
														.itemId(item)
														.couponCode(couponCode)
														.couponType(item.getItemType())
														.status("issued")
														.build();
		
		
		return couponRepository.save(entity);
	}

	@Override
	public Optional<CouponHistoryEntity> findByCouponCode(String couponCode) {
		
		CouponHistoryEntity coupon = couponRepository.findByCouponCode(couponCode)
													 .orElseThrow(() -> new RuntimeException("존재하지 않느 쿠폰 코드입니다."));
		
		coupon.setStatus("used");
		coupon.setUsedDate(LocalDateTime.now());
		
		CouponHistoryEntity saved = couponRepository.save(coupon);
		
		return  Optional.of(saved);
	}

	@Override
	public List<CouponHistoryEntity> getCouponsByMember(Long memberId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void useCoupon(String couponCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValidCoupon(String couponCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int expireOldCoupons() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CouponHistoryEntity> getAllCouponHistories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CouponStatusDTO getCouponStatsByItem(Long itemId) {
		// TODO Auto-generated method stub
		return null;
	}

}
