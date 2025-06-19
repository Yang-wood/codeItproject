package com.codeit.mini.service.vending;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.vending.VendingItemDTO;
import com.codeit.mini.dto.vending.VendingMachineDTO;
import com.codeit.mini.dto.vending.VendingResultDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.comm.VendingType;
import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;

import jakarta.annotation.Nullable;

public interface IVendingMachineService {
	
	Long registerVendingMachine(VendingMachineDTO vmDto);
	
	Optional<VendingMachineDTO> findVendingMachineById(Long vmId);
	
	List<VendingMachineDTO> findAllVendingMachineById(Long vmId);
	
	PageResultDTO<VendingMachineDTO, VendingMachinesEntity> getVendingMachines(PageRequestDTO requestDTO);
	
	PageResultDTO<VendingMachineDTO, VendingMachinesEntity> getVendingMachinesWithFilter(PageRequestDTO requestDTO);
	
	VendingMachineDTO updateVendingMachine(VendingMachineDTO vmDto);
	
	boolean changeActiveVendingMahchine(Long vmId);
	
	boolean removeVendingMachine(Long vmId);
	
	VendingResultDTO useVendingMachine(Long vmId, Long memberId, @Nullable Long itemId);
	
	VendingResultDTO purchaseMultipleItems(Long vmId, Long memberId, List<Long> itemIds);
	
	default VendingMachineDTO toDTO(VendingMachinesEntity vm) {
		
		VendingMachineDTO dto = VendingMachineDTO.builder()
													  .machineId(vm.getMachineId())
													  .adminId(vm.getAdminId().getAdminId())
													  .name(vm.getName())
													  .description(vm.getDescription())
													  .type(vm.getType().name())
													  .active(vm.getIsActive())
													  .regDate(vm.getRegDate())
													  .build();
		return dto;
	}
	
	default VendingMachineDTO toDetail(VendingMachinesEntity vm, List<MachineItemEntity> machineItems) {
		List<VendingItemDTO> items = machineItems.stream().map(mi -> {
									 VendingItemEntity item = mi.getVendingItem();
									 return VendingItemDTO.builder().itemId(item.getItemId())
											 						.name(item.getName())
											 						.itemType(item.getItemType())
											 						.value(item.getValue())
											 						.probability(mi.getProbability())
											 						.build();
		}).collect(Collectors.toList());
		
		return VendingMachineDTO.builder().machineId(vm.getMachineId())
	            				.adminId(vm.getAdminId().getAdminId())
	            				.name(vm.getName())
	            				.description(vm.getDescription())
	            				.type(vm.getType().name())
	            				.active(vm.getIsActive())
	            				.regDate(vm.getRegDate())
	            				.items(items)
	            				.build();
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
