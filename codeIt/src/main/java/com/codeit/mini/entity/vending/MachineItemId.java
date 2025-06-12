package com.codeit.mini.entity.vending;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class MachineItemId implements Serializable{

	@Column(name = "machine_id")
	private Long machineId;
	
	@Column(name = "item_id")
	private Long itemId;
	
	public MachineItemId() {
		
	}
	public MachineItemId(Long machineId, Long itemId) {
		this.machineId = machineId;
		this.itemId = itemId;
	}
	
}
