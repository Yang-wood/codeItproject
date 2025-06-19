package com.codeit.mini.repository.vending;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.vending.search.IVendingMachinesSearch;

public interface IVendingMachinesRepository extends JpaRepository<VendingMachinesEntity, Long> {

	Optional<VendingMachinesEntity> findByName(String name);
	
	Optional<VendingMachinesEntity> findByMachineIdAndIsActive(Long machineId, Integer isActive);
	
	Page<VendingMachinesEntity> findByIsActive(int isActive, Pageable pageable);

	@Modifying
	@Query("UPDATE VendingMachinesEntity vm "
		 + "SET vm.isActive = 0 "
		 + "WHERE vm.machineId = :machineId")
	void deactivateMachine(@Param("machineId") Long machineId);
	
}
