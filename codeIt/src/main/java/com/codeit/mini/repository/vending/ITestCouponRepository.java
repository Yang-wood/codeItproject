package com.codeit.mini.repository.vending;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.entity.comm.CouponStatusEnum;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.TestCouponEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;

public interface ITestCouponRepository extends JpaRepository<TestCouponEntity, Long> {

    Optional<TestCouponEntity> findByCouponCode(String couponCode);

    boolean existsByCouponCode(String couponCode);

    boolean existsByMemberIdAndItemId(MemberEntity member, VendingItemEntity item);
    
    @Query("SELECT c FROM TestCouponEntity c "
       	 + "WHERE c.memberId.memberId = :memberId "
       	 + "AND c.itemId.itemId = :itemId "
       	 + "AND c.status = :status "
       	 + "ORDER BY CASE WHEN c.expireDate IS NULL THEN 1 ELSE 0 END, c.expireDate ASC")
       Optional<TestCouponEntity> findUsableCouponWithNearestExpireDate(@Param("memberId") Long memberId,
   																	 @Param("itemId") Long itemId,
   																	 @Param("status") CouponStatusEnum status);
}
