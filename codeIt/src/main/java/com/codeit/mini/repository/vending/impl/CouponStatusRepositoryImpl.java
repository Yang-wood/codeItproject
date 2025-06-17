package com.codeit.mini.repository.vending.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.codeit.mini.dto.vending.CouponHistoryRequestDTO;
import com.codeit.mini.dto.vending.CouponStatusDTO;
import com.codeit.mini.entity.comm.CouponStatusEnum;
import com.codeit.mini.entity.vending.QCouponHistoryEntity;
import com.codeit.mini.repository.vending.ICouponStatusRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CouponStatusRepositoryImpl implements ICouponStatusRepository {
	
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public CouponStatusDTO getCouponStatsByItem(Long itemId) {
		
		QCouponHistoryEntity c = QCouponHistoryEntity.couponHistoryEntity;
		
		NumberExpression<Integer> issuedCnt = new CaseBuilder().when(c.status.eq(CouponStatusEnum.ISSUED.name()))
															   .then(1)
															   .otherwise(0)
															   .sum();

		NumberExpression<Integer> usedCnt = new CaseBuilder().when(c.status.eq(CouponStatusEnum.USED.name()))
															 .then(1)
															 .otherwise(0)
															 .sum();
		
		NumberExpression<Integer> expiredCnt = new CaseBuilder().when(c.status.eq(CouponStatusEnum.EXPIRED.name()))
																.then(1)
																.otherwise(0)
																.sum();
		
		return jpaQueryFactory.select(Projections.constructor(
									  CouponStatusDTO.class, c.itemId.itemId,
									  						 c.itemId.name,
									  						 issuedCnt,
									  						 usedCnt,
									  						 expiredCnt
							  ))
							  .from(c)
							  .where(c.itemId.itemId.eq(itemId))
							  .groupBy(c.itemId.itemId, c.itemId.name)
							  .fetchOne();
	}
	
	public OrderSpecifier<?> resolveOrder(String field, String direction) {
	    QCouponHistoryEntity c = QCouponHistoryEntity.couponHistoryEntity;
	    Order order = "desc".equalsIgnoreCase(direction) ? Order.DESC : Order.ASC;

	    switch (field) {
	        case "couponCode": return new OrderSpecifier<>(order, c.couponCode);
	        case "status": return new OrderSpecifier<>(order, c.status);
	        case "name": return new OrderSpecifier<>(order, c.itemId.name);
	        case "issuedDate":
	        default: return new OrderSpecifier<>(order, c.issuedDate);
	    }
	}

	@Override
	public List<CouponStatusDTO> getCouponStats(CouponHistoryRequestDTO request) {
		
		QCouponHistoryEntity c = QCouponHistoryEntity.couponHistoryEntity;
		BooleanBuilder builder = new BooleanBuilder();

		if (request.getStatus() != null) {
		    builder.and(c.status.eq(request.getStatus().name()));
		}
		
		if (StringUtils.hasText(request.getKeyword())) {
		    builder.and(c.couponCode.containsIgnoreCase(request.getKeyword())
		    	   .or(c.itemId.name.containsIgnoreCase(request.getKeyword()))
		           .or(c.couponId.stringValue().containsIgnoreCase(request.getKeyword())));
		}

		if (request.getFromDate() != null && request.getToDate() != null) {
		    builder.and(c.issuedDate.between(request.getFromDate().atStartOfDay(),
		    								 request.getToDate().atTime(23, 59, 59)));
		}

		NumberExpression<Integer> issuedCnt = new CaseBuilder().when(c.status.eq(CouponStatusEnum.ISSUED.name()))
															   .then(1).otherwise(0).sum();

		NumberExpression<Integer> usedCnt = new CaseBuilder().when(c.status.eq(CouponStatusEnum.USED.name()))
															 .then(1).otherwise(0).sum();

		NumberExpression<Integer> expiredCnt = new CaseBuilder().when(c.status.eq(CouponStatusEnum.EXPIRED.name()))
																.then(1).otherwise(0).sum();

		return jpaQueryFactory.select(Projections.constructor(CouponStatusDTO.class, c.itemId.itemId,
																			         c.itemId.name,
																			         issuedCnt,
																			         usedCnt,
																			         expiredCnt))
											     .from(c)
											     .where(builder)
											     .groupBy(c.itemId.itemId, c.itemId.name)
											     .fetch();
	}
	
	public Page<CouponStatusDTO> getCouponStatsPage(CouponHistoryRequestDTO request) {
		
		QCouponHistoryEntity c = QCouponHistoryEntity.couponHistoryEntity;
		BooleanBuilder builder = new BooleanBuilder();
		
		if (request.getStatus() != null) {
		    builder.and(c.status.eq(request.getStatus().name()));
		}
		
	    if (StringUtils.hasText(request.getKeyword())) {
	    	builder.and(c.couponCode.containsIgnoreCase(request.getKeyword())
	    		   .or(c.itemId.name.containsIgnoreCase(request.getKeyword()))
	    		   .or(c.couponId.stringValue().contains(request.getKeyword())));
	    }
	    
	    if (request.getFromDate() != null && request.getToDate() != null) {
	    	builder.and(c.issuedDate.between(request.getFromDate().atStartOfDay(), request.getToDate().atTime(23, 59, 59)));
	    }

		NumberExpression<Integer> issuedCnt = new CaseBuilder().when(c.status.eq(CouponStatusEnum.ISSUED.name()))
															   .then(1).otherwise(0).sum();

		NumberExpression<Integer> usedCnt = new CaseBuilder().when(c.status.eq(CouponStatusEnum.USED.name()))
				 											 .then(1).otherwise(0).sum();

		NumberExpression<Integer> expiredCnt = new CaseBuilder().when(c.status.eq(CouponStatusEnum.EXPIRED.name()))
																.then(1).otherwise(0).sum();
		
		int offset = request.getPage() * request.getSize();
		int limit = request.getSize();
		
		OrderSpecifier<?> orderSpecifier = resolveOrder(request.getType(), request.getSort());
		
		List<CouponStatusDTO> content = jpaQueryFactory.select(Projections.constructor
															  (CouponStatusDTO.class, c.itemId.itemId,
																					  c.itemId.name,
																					  issuedCnt,
																					  usedCnt,
																					  expiredCnt))
													   .from(c)
													   .where(builder)
													   .groupBy(c.itemId.itemId, c.itemId.name)
													   .offset(offset)
													   .orderBy(orderSpecifier)
													   .limit(limit)
													   .fetch();
		
		long total = jpaQueryFactory.select(c.itemId.itemId.countDistinct())
									.from(c)
									.where(builder)
									.fetchOne();

	    return new PageImpl<>(content, PageRequest.of(request.getPage(),
	    											  request.getSize(),
	    											  Sort.by(request.getSort(), request.getType())), total);
	}

}
