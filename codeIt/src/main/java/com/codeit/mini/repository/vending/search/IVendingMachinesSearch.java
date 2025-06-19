package com.codeit.mini.repository.vending.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.entity.vending.VendingMachinesEntity;

public interface IVendingMachinesSearch {

	Page<VendingMachinesEntity> searchMachines(PageRequestDTO requestDTO, Pageable pageable);
}
