package com.codeit.mini.dto.vending;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendingMachineUpdateRequestDTO {
	
    private VendingMachineDTO vendingMachine;
    private List<RequestMachineItemDTO> itemIds;
}
