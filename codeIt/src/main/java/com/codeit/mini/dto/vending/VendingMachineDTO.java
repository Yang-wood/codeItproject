package com.codeit.mini.dto.vending;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendingMachineDTO {

	private Long machineId;
	private Long adminId;
	private String name;
	private String type;
	private String description;
	private Integer active;
	
	private LocalDateTime regDate;
	
	private List<VendingItemDTO> items;
}
