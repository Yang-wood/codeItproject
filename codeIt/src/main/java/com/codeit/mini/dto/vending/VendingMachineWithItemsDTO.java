package com.codeit.mini.dto.vending;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendingMachineWithItemsDTO {
	
	private VendingMachineDTO vendingMachine;
	
    private List<MachineItemDTO> itemIds;
    
}
