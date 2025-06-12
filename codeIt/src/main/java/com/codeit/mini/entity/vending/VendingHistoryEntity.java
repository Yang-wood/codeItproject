package com.codeit.mini.entity.vending;

import com.codeit.mini.entity.comm.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vending_history")
public class VendingHistoryEntity extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VENDING_HISTORY_SEQ")
	@Column(name = "history_id")
	private Long historyId;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "member_id")
//	private MemberEntity memberId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	VendingItemEntity itemId;
	
	String payment;
	
	String status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "point_id")
	PointHistoryEntity pointId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id")
	CouponHistoryEntity couponId;
	
}
