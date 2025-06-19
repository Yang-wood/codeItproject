package com.codeit.mini.entity.vending;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
@Data
public class MachineItemId implements Serializable{

	@Column(name = "machine_id")
	private Long machineId;
	
	@Column(name = "item_id")
	private Long itemId;
	
	public MachineItemId(Long machineId, Long itemId) {
		this.machineId = machineId;
		this.itemId = itemId;
	}
	

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MachineItemId)) return false;
        MachineItemId that = (MachineItemId) o;
        return Objects.equals(machineId, that.machineId) &&
               Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machineId, itemId);
    }
	
}
