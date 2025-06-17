package com.codeit.mini.repository.vending;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.entity.comm.CouponStatusEnum;
import com.codeit.mini.entity.vending.TestCouponEntity;
import com.codeit.mini.entity.vending.TestCouponHistoryEntity;

public interface ITestCouponHistoryRepository extends JpaRepository<TestCouponHistoryEntity, Long> {

	Page<TestCouponHistoryEntity> findByTestCouponId_TestCouponId(Long couponId, Pageable pageable);

    Page<TestCouponHistoryEntity> findByTestCouponId_MemberId_MemberId(Long memberId, Pageable pageable);

    Page<TestCouponHistoryEntity> findByTestCouponId_CouponCode(String couponCode, Pageable pageable);
    
    @Query("SELECT h FROM TestCouponHistoryEntity h WHERE "
    	 + "(:memberId IS NULL OR h.memberId.memberId = :memberId) AND "
    	 + "(:status IS NULL OR h.status = :status) AND "
    	 + "(:start IS NULL OR h.regDate >= :start) AND "
    	 + "(:end IS NULL OR h.regDate <= :end)")
	Page<TestCouponHistoryEntity> searchWithFilters(@Param("memberId") Long memberId,
													@Param("status") String status,
													@Param("start") LocalDateTime start,
													@Param("end") LocalDateTime end,
													Pageable pageable);
}
