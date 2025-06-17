package com.codeit.mini.repository.vending;

import java.util.List;
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

    Optional<TestCouponEntity> findByMemberIdAndItemId(MemberEntity member, VendingItemEntity item);
    
    boolean existsByMemberIdAndItemId(MemberEntity member, VendingItemEntity item);
    
    @Query("SELECT c FROM TestCouponEntity c "
    	 + "WHERE c.memberId.memberId = :memberId "
    	 + "AND (:itemId IS NULL OR c.itemId.itemId = :itemId) "
    	 + "AND c.status = :status "
    	 + "AND (:type IS NULL OR c.itemId.itemType = :type) "
    	 + "ORDER BY c.expireDate ASC NULLS LAST")
    Optional<TestCouponEntity> findUsableCouponWithNearestExpireDate(@Param("memberId") Long memberId,
    	        													 @Param("itemId") Long itemId,
    	        													 @Param("status") CouponStatusEnum status,
    	        													 @Param("type") String itemType);
    
    @Query("SELECT c FROM TestCouponEntity c "
       	 + "WHERE c.memberId.memberId = :memberId "
       	 + "AND c.itemId.itemId = :itemId "
       	 + "AND c.status = :status "
       	 + "ORDER BY CASE WHEN c.expireDate IS NULL THEN 1 ELSE 0 END, c.expireDate ASC")
       Optional<TestCouponEntity> findUsableCouponWithNearestExpireDate(@Param("memberId") Long memberId,
   																	 @Param("itemId") Long itemId,
   																	 @Param("status") CouponStatusEnum status);

}
