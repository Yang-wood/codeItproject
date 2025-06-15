package com.codeit.mini.repository.vending;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.mini.entity.vending.VendingHistoryEntity;

public interface IVendingHistoryRepository extends JpaRepository<VendingHistoryEntity, Long>{

}
