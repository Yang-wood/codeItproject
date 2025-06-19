package com.codeit.mini.dto.vending;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class MachineItemDTO {

	private Long machineId;
	private Long itemId;
	private double probability;
	private String name;
	private String description;
	private int value;
}
