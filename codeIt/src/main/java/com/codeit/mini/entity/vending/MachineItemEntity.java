package com.codeit.mini.entity.vending;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "machine_item")
public class MachineItemEntity {

	@EmbeddedId
	private MachineItemId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("machineId")
	@JoinColumn(name = "machine_id")
	private VendingMachinesEntity vendingMachine;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("itemId")
	@JoinColumn(name = "itme_id")
	private VendingItemEntity vendingItem;
	
	@Column(name = "probability")
	private double probability;
	
}