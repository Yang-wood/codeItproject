package com.codeit.mini.dto.vending;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class VendingItemDTO {

	private Long itemId;
	private Long adminId;
	private String name;
	private String description;
	private String item_type;
	private String value;
	private double probability;
	private Integer stock;
	private Integer total_used;
	private Integer total_claim;
	private Integer active;
	private LocalDateTime regdate;
	private LocalDateTime updatedate;
	
}
