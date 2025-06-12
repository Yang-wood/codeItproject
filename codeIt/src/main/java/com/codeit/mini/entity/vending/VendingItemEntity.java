package com.codeit.mini.entity.vending;

import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.comm.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@ToString (exclude = "adminId")
@Table (name = "vending_item")
public class VendingItemEntity extends BaseEntity{
	
	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "ITEM_ID_SEQ")
	@Column(name = "item_id", nullable = false)
	private Long itemId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id", foreignKey = @ForeignKey(name = "fk_vm_item_admin_id"))
	private Admin adminId;
	
	@Column(name = "name", nullable = false, length = 50)
	private String name;
	
	@Column(name = "description", length = 300)
	private String description;

	@Column(name = "item_type", length = 30)
	private String itemType;

	@Column(name = "value")
	private int value;
	
	@Column(name = "probability")
	private Double probability;

	@Column(name = "stock")
	private Integer stock;
	
    @Builder.Default
    @Column(name = "total_used", nullable = false)
    private Integer totalUsed = 0;

	@Builder.Default
    @Column(name = "total_claim", nullable = false)
    private Integer totalClaim = 0;
	
//	1: 활성 / 0 : 비활성
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Integer isActive = 1;

}
