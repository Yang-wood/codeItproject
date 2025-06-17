package com.codeit.mini.repository.vending;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.entity.vending.VendingHistoryEntity;

public interface IVendingHistoryRepository extends JpaRepository<VendingHistoryEntity, Long>{

	Page<VendingHistoryEntity> findByMemberId_MemberId(Long memberId, Pageable pageable);
	
	Page<VendingHistoryEntity> findAllByItemId_ItemId(Long itemId, Pageable pageable);
	
	Page<VendingHistoryEntity> findAllByCouponIdIsNotNull(Pageable pageable);
	
	@Query("SELECT vh "
		 + "FROM VendingHistoryEntity vh "
		 + "WHERE vh.itemId.itemId = :itemId AND vh.itemId "
		 + "IN (SELECT mi.vendingItem "
		 	 + "FROM MachineItemEntity mi "
		 	 + "WHERE mi.vendingMachine.machineId = :machineId)")
	Page<VendingHistoryEntity> findByMachineIdAndItemId(@Param("machineId") Long machineId,
														@Param("itemId") Long itemId,
														Pageable pageable);
	
	@Query("SELECT COUNT(v) "
		 + "FROM VendingHistoryEntity v "
		 + "WHERE v.memberId.memberId = :memberId")
	long countByMember(@Param("memberId") Long memberId);
	
	@Query("SELECT COUNT(v) "
		 + "FROM VendingHistoryEntity v "
		 + "WHERE v.itemId.itemId IN ( "
		 + "SELECT mi.vendingItem.itemId "
		 + "FROM MachineItemEntity mi "
		 + "WHERE mi.vendingMachine.machineId = :machineId )")
	long countByMachine(@Param("machineId") Long machineId);
	
	@Query("SELECT COALESCE(SUM(p.amount), 0) "
		 + "FROM VendingHistoryEntity v "
		 + "JOIN v.pointId p "
		 + "WHERE v.memberId.memberId = :memberId AND p.amount < 0")
	int sumUsedPointsByMember(@Param("memberId") Long memberId);
	
	@Query("SELECT COALESCE(SUM(p.amount), 0) "
		 + "FROM VendingHistoryEntity v "
		 + "JOIN v.pointId p "
		 + "WHERE v.memberId.memberId = :memberId AND p.amount > 0")
	int sumRewardedPointsByMember(@Param("memberId") Long memberId);
	
	@Query("SELECT v "
		+ " FROM VendingHistoryEntity v "
		+ " WHERE (:memberId IS NULL OR v.memberId.memberId = :memberId) "
		+ " AND (:machineId IS NULL OR v.itemId.itemId IN "
				+ " (SELECT mi.vendingItem.itemId FROM MachineItemEntity mi "
				+ "  WHERE mi.vendingMachine.machineId = :machineId))"
		+ " AND (:payment IS NULL OR v.payment = :payment) "
		+ " AND (:status IS NULL OR v.status = :status)")
	Page<VendingHistoryEntity> searchHistoriesWithFilters(@Param("memberId") Long memberId,
		                                                  @Param("machineId") Long machineId,
		                                                  @Param("payment") String payment,
		                                                  @Param("status") String status, Pageable pageable);
}
