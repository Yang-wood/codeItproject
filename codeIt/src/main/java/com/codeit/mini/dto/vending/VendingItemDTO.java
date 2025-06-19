package com.codeit.mini.dto.vending;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendingItemDTO {

	private Long itemId;
	private Long adminId;
	private String name;
	private String description;
	private String itemType;
	private int value;
	private Double probability;
	private Integer stock;
	private Integer totalUsed;
	private Integer totalClaim;
	private Integer active;
	private LocalDateTime regDate;
	private LocalDateTime upDate;
	
}
