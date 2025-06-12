package com.codeit.mini.entity.vending;

import com.codeit.mini.entity.comm.CouponBaseDateEntity;
import com.codeit.mini.entity.member.MemberEntity;

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
		name = "COUPON_ID_SEQ",
		sequenceName = "coupon_id_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "coupon_history")
public class CouponHistoryEntity extends CouponBaseDateEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUPON_ID_SEQ")
	private Long couponId;
	
	@JoinColumn(name = "member_id")
	private MemberEntity memberId;
	
	@JoinColumn(name = "itme_id")
	private VendingItemEntity itemId;
	
	@Column(nullable = false, unique = true ,length = 40)
	private String couponCode;
	
	@Column(nullable = false, length = 20)
	private String type;
	
	@Column(nullable = false, length = 20)
	private String status;
	
//	쿠폰 코드 생성 메소드 호출
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
}
