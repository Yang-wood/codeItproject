package com.codeit.mini.dto.vending;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VendingResultDTO {

	private String itemName;
	private String couponCode;
	private int costPoint;
	private int rewardPoint;
	private String message;
	private List<String> itemList;
	
}
