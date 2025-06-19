package com.codeit.mini.repository.vending;

import lombok.Data;

@Data
public class MachineItemRequestDTO {

    private Long itemId;
    private Double probability;
}
