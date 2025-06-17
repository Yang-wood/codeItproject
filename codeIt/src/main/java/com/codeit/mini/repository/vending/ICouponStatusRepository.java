package com.codeit.mini.repository.vending;

import java.util.List;

import org.springframework.data.domain.Page;

import com.codeit.mini.dto.vending.CouponHistoryRequestDTO;
import com.codeit.mini.dto.vending.CouponStatusDTO;
import com.querydsl.core.types.OrderSpecifier;

public interface ICouponStatusRepository {

	CouponStatusDTO getCouponStatsByItem(Long itemId);
	
	OrderSpecifier<?> resolveOrder(String field, String direction);
	
	List<CouponStatusDTO> getCouponStats(CouponHistoryRequestDTO request);
	
	Page<CouponStatusDTO> getCouponStatsPage(CouponHistoryRequestDTO request);
	
}
