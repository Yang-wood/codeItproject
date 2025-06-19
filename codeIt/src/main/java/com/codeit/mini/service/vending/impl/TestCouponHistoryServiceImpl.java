package com.codeit.mini.service.vending.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.vending.TestCouponHistoryDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.TestCouponEntity;
import com.codeit.mini.entity.vending.TestCouponHistoryEntity;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.vending.ITestCouponHistoryRepository;
import com.codeit.mini.repository.vending.ITestCouponRepository;
import com.codeit.mini.service.vending.ITestCouponHistoryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class TestCouponHistoryServiceImpl implements ITestCouponHistoryService {
	
	private final ITestCouponHistoryRepository historyRepository;
    private final IMemberRepository memberRepository;
    private final ITestCouponRepository testCouponRepository;
    
    @Transactional
	@Override
	public TestCouponHistoryDTO saveHistory(Long memberId, Long couponId, String couponCode, String status) {
    	log.info("🟡 [saveHistory] 호출됨 - memberId: {}, couponId: {}, status: {}", memberId, couponId, status);
    	
    	MemberEntity member = memberRepository.findById(memberId)
                							  .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        TestCouponEntity coupon = testCouponRepository.findById(couponId)
                									  .orElseThrow(() -> new RuntimeException("쿠폰이 존재하지 않습니다."));

        TestCouponHistoryEntity entity = TestCouponHistoryEntity.builder()
												                .memberId(member)
												                .testCouponId(coupon)
												                .couponCode(coupon.getCouponCode())
												                .status(status)
												                .build();
        log.info("🧾 저장 전 히스토리 - member: {}, couponId: {}, status: {}", memberId, couponId, status);
        return toDTO(historyRepository.save(entity));
	}
	
	@Override
	public PageResultDTO<TestCouponHistoryDTO, TestCouponHistoryEntity> getHistoriesByMember(Long memberId, PageRequestDTO requestDTO) {
    	Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());
        
    	Page<TestCouponHistoryEntity> result = historyRepository.findByTestCouponId_MemberId_MemberId(memberId, pageable);
        
    	return new PageResultDTO<>(result, this::toDTO);
	}

    @Override
    public PageResultDTO<TestCouponHistoryDTO, TestCouponHistoryEntity> getHistoriesByCouponCode(String couponCode, PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());
        
        Page<TestCouponHistoryEntity> result = historyRepository.findByCouponCodeCustom(couponCode, pageable);
        
        return new PageResultDTO<>(result, this::toDTO);
    }
    
}
