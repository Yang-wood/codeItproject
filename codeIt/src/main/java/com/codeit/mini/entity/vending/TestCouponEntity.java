package com.codeit.mini.entity.vending;

import com.codeit.mini.entity.comm.CouponBaseDateEntity;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@SequenceGenerator(
		name = "TEST_COUPON_ID_SEQ",
		sequenceName = "test_coupon_id_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"memberId", "itemId"})
@Table(name = "test_coupon",
		uniqueConstraints = {
		@UniqueConstraint(name = "uq_test_coupon_code", columnNames = "coupon_code")}
)
public class TestCouponEntity extends CouponBaseDateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_COUPON_ID_SEQ")
	private Long testCouponId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_member_id_test_coupon"))
	private MemberEntity memberId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false, foreignKey = @ForeignKey(name = "fk_item_id_test_coupon"))
	private VendingItemEntity itemId;
	
	@Column(name = "coupon_code", length = 60,nullable = false)
	private String couponCode;
	
	@Column(name = "total_count", nullable = false)
	private Integer totalCnt;
	
	@Column(name = "remain_count", nullable = false)
	private Integer remainCnt;
	
	@Builder.Default
	@Column(name = "status", length = 30)
	private String status = "issued";
	
}
