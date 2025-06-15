package com.codeit.mini.dto.vending;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class VendingMachineDTO {

	private Long machineId;
	private Long adminId;
	private String name;
	private String type;
	private String description;
	private Integer active;
	private LocalDateTime regDate;
	
}
