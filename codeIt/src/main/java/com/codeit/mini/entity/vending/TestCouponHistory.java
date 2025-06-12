package com.codeit.mini.entity.vending;

import com.codeit.mini.entity.MemberEntity;
import com.codeit.mini.entity.comm.CouponBaseDateEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@ToString
@Table(name = "test_coupon")
public class TestCouponHistory extends CouponBaseDateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_COUPON_ID_SEQ")
	private Long testCouponId;
	
	@JoinColumn(name = "member_id")
	private MemberEntity memberId;
	
	@JoinColumn(name = "item_id")
	private VendingItemEntity itemId;
	
	@Column(name = "coupon_code", nullable = false, unique = true)
	private String couponCode;
	
	@Column(nullable = false)
	private Integer totalCnt;
	
	@Column(nullable = false)
	private Integer remainCnt;
	
	private String status;
	
}
