package com.codeit.mini.entity.vending;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"vendingMachine", "vendingItem"})
@Table(name = "machine_item")
public class MachineItemEntity {

	@EmbeddedId
	private MachineItemId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("machineId")
	@JoinColumn(name = "machine_id", foreignKey = @ForeignKey(name = "fk_vm_item_vm_id"))
	private VendingMachinesEntity vendingMachine;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("itemId")
	@JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_vm_item_item_id"))
	private VendingItemEntity vendingItem;
	
	@Column(name = "probability")
	private Double probability;
	
}