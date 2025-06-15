package com.codeit.mini.entity.vending;

import com.codeit.mini.entity.comm.BaseEntity;
import com.codeit.mini.entity.member.MemberEntity;

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
@SequenceGenerator(
		name = "VENDING_HISTORY_SEQ",
		sequenceName = "vm_history_seq",
		initialValue = 1,
		allocationSize = 1
)
@Getter
@Setter
@ToString(exclude = {"memberId", "itemId", "pointId", "couponId"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vending_history")
public class VendingHistoryEntity extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VENDING_HISTORY_SEQ")
	@Column(name = "history_id")
	private Long historyId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_member_id_veding"))
	private MemberEntity memberId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false, foreignKey = @ForeignKey(name = "fk_item_id_veding"))
	private VendingItemEntity itemId;
	
	@Builder.Default
	@Column(name = "payment", length = 30)
	private String payment = "point";
	
	@Builder.Default
	@Column(name = "status", length = 30)
	private String status = "success";
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "point_id", nullable = true, foreignKey = @ForeignKey(name = "fk_point_id_veding"))
	private PointHistoryEntity pointId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id", nullable = true, foreignKey = @ForeignKey(name = "fk_coupon_id_veding"))
	private CouponHistoryEntity couponId;
	
}
