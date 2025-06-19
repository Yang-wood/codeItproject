package com.codeit.mini.dto.vending;

import java.util.List;

import lombok.Data;

@Data
public class VendingMachineWithItemsDTO {
	
	private VendingMachineDTO vendingMachine;
	
    private List<MachineItemDTO> itemIds;
    
}
