package com.codeit.mini.repository.vending;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.mini.entity.vending.VendingItemEntity;

public interface IVendingItemRepository extends JpaRepository<VendingItemEntity, Long>{

	Optional<VendingItemEntity> findByName(String name);
	
	@Modifying
	@Query("UPDATE VendingItemEntity item "
		 + "SET item.isActive = 0 "
		 + "WHERE item.itemId = :itemId ")
	int deactivateById(@Param("itemId") Long itemId);
	
	@Modifying
	@Query("UPDATE VendingItemEntity item "
			+ "SET item.isActive = 0 "
			+ "WHERE item.itemId IN :itemIds ")
	int deactivateByIds(@Param("itemId") List<Long> itemIds);
	
}