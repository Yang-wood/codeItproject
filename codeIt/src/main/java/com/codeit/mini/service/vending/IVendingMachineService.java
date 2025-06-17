package com.codeit.mini.service.vending;

import java.util.List;
import java.util.Optional;

import com.codeit.mini.dto.vending.VendingMachineDTO;
import com.codeit.mini.dto.vending.VendingResultDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.comm.VendingType;
import com.codeit.mini.entity.vending.VendingMachinesEntity;

import jakarta.annotation.Nullable;

public interface IVendingMachineService {
	
	Long registerVendingMachine(VendingMachineDTO vmDto);
	
	Optional<VendingMachineDTO> findVendingMachineById(Long vmId);
	
	List<VendingMachineDTO> findAllVendingMachineById(Long vmId);
	
	VendingMachineDTO updateVendingMachine(VendingMachineDTO vmDto);
	
	boolean changeActiveVendingMahchine(Long vmId);
	
	boolean removeVendingMachine(Long vmId);
	
	VendingResultDTO useVendingMachine(Long vmId, Long memberId, @Nullable Long itemId);
	
	default VendingMachineDTO toDTO(VendingMachinesEntity vm) {
		VendingMachineDTO machineDTO = VendingMachineDTO.builder()
														.machineId(vm.getMachineId())
														.adminId(vm.getAdminId().getAdminId())
														.name(vm.getName())
														.type(vm.getType().name())
														.build();
		return machineDTO;
	}
	
	default VendingMachinesEntity toEntity(VendingMachineDTO vmDto, Admin admin) {
		
		VendingMachinesEntity vmEntity = VendingMachinesEntity.builder()
															 .machineId(vmDto.getMachineId())
															 .adminId(admin)
															 .name(vmDto.getName())
															 .description(vmDto.getDescription())
															 .type(VendingType.valueOf(vmDto.getType()))
															 .build();
		return vmEntity;
	}
}
