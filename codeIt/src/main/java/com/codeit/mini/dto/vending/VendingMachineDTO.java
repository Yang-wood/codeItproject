package com.codeit.mini.dto.vending;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class VendingMachineDTO {

	Long machineId;
	String name;
	String type;
	String description;
	Integer active;
	
}
