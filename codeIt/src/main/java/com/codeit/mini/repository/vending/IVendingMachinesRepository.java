package com.codeit.mini.repository.vending;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.entity.vending.VendingMachinesEntity;

public interface IVendingMachinesRepository extends JpaRepository<VendingMachinesEntity, Long> {

	Optional<VendingMachinesEntity> findByName(String name);

	@Modifying
	@Query("UPDATE VendingMachinesEntity vm "
		 + "SET vm.isActive = 0 "
		 + "WHERE vm.machineId = :machineId")
	void deactivateMachine(@Param("machineId") Long machineId);
	
}
