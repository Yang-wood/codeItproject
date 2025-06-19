package com.codeit.mini.service.vending.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.comm.UnifiedCouponHistoryDTO;
import com.codeit.mini.dto.vending.CouponHistoryDTO;
import com.codeit.mini.dto.vending.CouponHistoryRequestDTO;
import com.codeit.mini.dto.vending.CouponStatusDTO;
import com.codeit.mini.entity.comm.CouponStatusEnum;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.CouponHistoryEntity;
import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.TestCouponEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.vending.ICouponHistoryRepository;
import com.codeit.mini.repository.vending.IMachineItemRepository;
import com.codeit.mini.repository.vending.ITestCouponRepository;
import com.codeit.mini.repository.vending.IVendingItemRepository;
import com.codeit.mini.repository.vending.querydsl.ICouponStatusRepository;
import com.codeit.mini.service.vending.ICouponHistoryService;
import com.codeit.mini.service.vending.ITestCouponService;
import com.codeit.mini.util.CouponCodeGenerator;
import com.codeit.mini.util.CouponUtils;

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
	private final ITestCouponRepository testCouponRepository;
	private final ITestCouponService testCouponService;

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
	    
	    if (itemType.equals("lose")) {
	        throw new IllegalStateException("꽝 아이템은 쿠폰을 발급할 수 없습니다.");
	    }
	    
	    if (itemType.equals("test") || itemType.equals("free")) {
	        testCouponService.issueTestCoupon(memberId, itemId, machineId);
	        return null;
	    }

	    boolean isRandom = vendingType.equals("random");
	    boolean isDuplicationAllowed = itemType.equals("rental") || itemType.equals("discount");
	    
	    int discountRate = 0;
	    if (itemType.equals("discount")) {
	        discountRate = CouponUtils.extractDiscountRate(item.getName());
	    }
	    
	    if (!(isRandom && isDuplicationAllowed)) {
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
	    entity.setDiscountRate(discountRate);
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

	@Override
	public Page<CouponHistoryEntity> getCouponHistoryByItem(Long itemId, Pageable pageable) {
		return couponRepository.findByItemId_ItemId(itemId, pageable);
	}

	@Override
	public PageResultDTO<UnifiedCouponHistoryDTO, UnifiedCouponHistoryDTO> getAllUserCouponsPaged(Long memberId, PageRequestDTO requestDTO) {
	    List<CouponHistoryEntity> oneTimeCoupons = couponRepository.findAllByMemberId_MemberIdOrderByIssuedDateDesc(memberId);

	    List<TestCouponEntity> testCoupons = testCouponRepository.findAllByMemberId_MemberId(memberId);

	    List<UnifiedCouponHistoryDTO> allHistories = new ArrayList<>();

	    for (CouponHistoryEntity c : oneTimeCoupons) {
	        allHistories.add(UnifiedCouponHistoryDTO.builder()
	                .type("일회성")
	                .couponCode(c.getCouponCode())
	                .status(c.getStatus().name())
	                .issuedDate(c.getIssuedDate())
	                .expireDate(c.getExpireDate())
	                .build());
	    }

	    for (TestCouponEntity t : testCoupons) {
	        allHistories.add(UnifiedCouponHistoryDTO.builder()
	                .type("응시")
	                .couponCode(t.getCouponCode())
	                .status(t.getStatus().name())
	                .issuedDate(t.getIssuedDate())
	                .expireDate(t.getExpireDate())
	                .build());
	    }

	    allHistories.sort(Comparator.comparing(UnifiedCouponHistoryDTO::getIssuedDate).reversed());

	    int page = requestDTO.getPage();
	    int size = requestDTO.getSize();

	    int start = (page - 1) * size;
	    int end = Math.min(start + size, allHistories.size());

	    List<UnifiedCouponHistoryDTO> pagedList = allHistories.subList(start, end);

	    PageImpl<UnifiedCouponHistoryDTO> pageResult = new PageImpl<>(pagedList, requestDTO.getPageable(Sort.by("issuedDate").descending()), allHistories.size());

	    return new PageResultDTO<>(pageResult, Function.identity());
	}

}