package com.codeit.mini.repository.vending;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.mini.entity.vending.CouponHistoryEntity;

public interface ICouponHistoryRepository extends JpaRepository<CouponHistoryEntity, Long>{

	Optional<CouponHistoryEntity> findByCouponCode(String couponCode);
}
