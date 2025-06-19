package com.codeit.mini.service.vending;

import java.util.List;
import java.util.Optional;

import com.codeit.mini.dto.vending.MachineItemDTO;
import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.MachineItemId;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;

public interface IMachineItemService {

	Long registerMachineItem(MachineItemDTO dto);
	
	List<Long> registerMachineItems(List<MachineItemDTO> dtoList);

	Optional<MachineItemDTO> findMachineItemById(Long machineId, Long itemId);

	List<MachineItemDTO> findAllItemsByMachineId(Long machineId);

	MachineItemDTO updateMachineItem(MachineItemDTO dto);
	
	void changeDeactiveMachine(Long machineId);

	void changeActivateMachine(Long machineId);
	
	boolean removeMachineItemId(Long machineId, Long itemId);
	
	boolean removeVendingItem(Long itemId);
	
	void removeAllByMachineId(Long machineId);
	
	
	default MachineItemDTO toDTO (MachineItemEntity entity) {
		MachineItemDTO vmToItemDTO = MachineItemDTO.builder()
												   .machineId(entity.getId().getMachineId())
												   .itemId(entity.getId().getItemId())
												   .probability(entity.getProbability() != null ? entity.getProbability() : 0.0)
												   .build();
		
		VendingItemEntity item = entity.getVendingItem();
		
		vmToItemDTO.setName(item.getName());
		vmToItemDTO.setDescription(item.getDescription());
		vmToItemDTO.setValue(item.getValue());
		
		return vmToItemDTO;
	}
	
	default MachineItemEntity toEntity (MachineItemDTO dto, VendingItemEntity itemEntity, VendingMachinesEntity vmEntity) {
		
		MachineItemEntity vmToItemEntity = MachineItemEntity.builder()
															.vendingMachine(vmEntity)
															.vendingItem(itemEntity)
															.probability(dto.getProbability())
															.build();
		return vmToItemEntity;
	}

}
