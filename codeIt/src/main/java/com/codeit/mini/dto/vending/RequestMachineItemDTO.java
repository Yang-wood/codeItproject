package com.codeit.mini.dto.vending;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMachineItemDTO {
	
    private Long itemId;
    private double probability;
}