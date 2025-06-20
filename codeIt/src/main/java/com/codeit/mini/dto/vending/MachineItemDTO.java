package com.codeit.mini.dto.vending;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MachineItemDTO {

	private Long machineId;
	private Long itemId;
	private String itemType;
	private double probability;
	private String name;
	private String description;
	private int value;
	
	private List<MachineItemDTO> items;
	
}
