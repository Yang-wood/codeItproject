package com.codeit.mini.entity.vending;

import com.codeit.mini.entity.comm.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@SequenceGenerator (
		name = "ITEM_ID_SEQ",
		sequenceName = "item_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table (name = "vending_machine")
public class VendingItemEntity extends BaseEntity{
	
	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "ITEM_ID_SEQ")
	@Column(name = "item_id", nullable = false)
	private Long itemId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "admin_id")
	private AdminEntity adminId;
	
	@Column(name = "name", nullable = false, length = 50)
	private String name;
	
	@Column(name = "description", length = 300)
	private String description;

	@Column(name = "item_type", length = 30)
	private String item_type;

	@Column(name = "value")
	private int value;
	
	@Column(name = "probability")
	private double probability;

	@Column(name = "stock")
	private Integer stock;
	
	@Column(name = "total_used")
	private Integer totalUsed;

	@Column(name = "total_claim")
	private Integer totalClaim;
	
	@Column(name = "is_active")
	private Integer active;

}
