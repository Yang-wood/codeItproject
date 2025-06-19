package com.codeit.mini.repository.vending;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.entity.comm.CouponStatusEnum;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.CouponHistoryEntity;
import com.codeit.mini.repository.vending.querydsl.ICouponStatusRepository;

public interface ICouponHistoryRepository extends JpaRepository<CouponHistoryEntity, Long> {

	Optional<CouponHistoryEntity> findByCouponCode(String couponCode);
	
	List<CouponHistoryEntity> findAllByMemberId_MemberIdOrderByIssuedDateDesc(Long memberId);
	
	List<CouponHistoryEntity> findAllByStatusAndExpireDateBefore(CouponStatusEnum status, LocalDateTime dateTime);

	long countByStatus(CouponStatusEnum status);
	
	boolean existsByMemberId_MemberIdAndItemId_ItemIdAndStatus(Long memberId, Long itemId, CouponStatusEnum status);

    boolean existsByCouponCode(String couponCode);
    
    List<CouponHistoryEntity> findAllByStatus(CouponStatusEnum status);
    
    Page<CouponHistoryEntity> findByMemberId_MemberId(Long memberId, Pageable pageable);
    
    List<CouponHistoryEntity> findAllByMemberId(MemberEntity member);
    
    Page<CouponHistoryEntity> findByItemId_ItemId(Long itemId, Pageable pageable);
    
    @Query("SELECT c FROM CouponHistoryEntity c " +
    	   "JOIN MachineItemEntity mi ON mi.vendingItem = c.itemId " +
    	   "WHERE mi.vendingMachine.machineId = :machineId")
    Page<CouponHistoryEntity> findAllByMachineId(@Param("machineId") Long machineId, Pageable pageable);
}
