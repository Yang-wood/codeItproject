package com.codeit.mini.service.vending;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponScheduler {
	
	private final ICouponHistoryService couponService;
	
	@Scheduled(cron = "0 0 1 * * ?")
	@Transactional
	public void expireOldCoupons() {
		
		int expiredCount = couponService.expireOldCoupons();
		System.out.println("[스케줄러] 만료된 쿠폰 수: " + expiredCount + "건");
	}

}
