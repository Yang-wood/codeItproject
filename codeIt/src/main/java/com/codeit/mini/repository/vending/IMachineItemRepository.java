package com.codeit.mini.repository.vending;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.MachineItemId;

import jakarta.persistence.LockModeType;

public interface IMachineItemRepository extends JpaRepository<MachineItemEntity, MachineItemId>{

	@Query("SELECT mi FROM MachineItemEntity mi JOIN FETCH mi.vendingItem vi WHERE mi.id.machineId = :machineId")
    List<MachineItemEntity> findAllWithItemByMachineId(@Param("machineId") Long machineId);
	
	List<MachineItemEntity> findByVendingMachine_MachineId(Long machineId);
	
	List<MachineItemEntity> findByVendingItem_ItemId(Long itemId);
	
	int deleteByVendingMachine_MachineId(Long mamachineId);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT mi FROM MachineItemEntity mi WHERE mi.vendingMachine.machineId = :vmId")
	List<MachineItemEntity> findAllByVendingMachineWithLock(@Param("vmId") Long vmId);
	
	@Modifying
	@Query("DELETE FROM MachineItemEntity mi "
		 + "WHERE mi.vendingMachine.machineId = :machineId")
	int deleteByMachineId(@Param("machineId") Long machineId);
	
	@Modifying
	@Query("DELETE FROM MachineItemEntity mi "
		 + "WHERE mi.vendingItem.itemId = :itemId")
	int deleteByItemId(@Param("itemId") Long itemId);
	
	@Query("SELECT mi FROM MachineItemEntity mi "
	     + "WHERE mi.vendingMachine.machineId = :machineId "
		 + "AND mi.vendingItem.itemId = :itemId")
	Optional<MachineItemEntity> findMachineItem(@Param("machineId") Long machineId,
												@Param("itemId") Long itemId);
	
}
