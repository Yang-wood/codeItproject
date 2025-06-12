package com.codeit.mini.dto.vending;

import java.time.LocalDateTime;

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
}
