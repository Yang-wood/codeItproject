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
@ToString (exclude = {"memberId", "itemId"})
@Table(name = "coupon_history",
		uniqueConstraints = {
				@UniqueConstraint(name = "uq_coupon_code", columnNames = "coupon_code")
		})
public class CouponHistoryEntity extends CouponBaseDateEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUPON_ID_SEQ")
	private Long couponId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_member_id_coupon"), nullable = false)
	private MemberEntity memberId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_item_id_coupon"), nullable = false)
	private VendingItemEntity itemId;
	
	@Column(name = "coupon_code", nullable = false,length = 40)
	private String couponCode;
	
//	대여, 문제풀이, 할인
	@Column(name = "coupon_type", nullable = false, length = 20)
	private String couponType;
	
	@Builder.Default
	@Column(name = "status", length = 20, nullable = false)
	private String status = "issued";
	
//	쿠폰 코드 생성 메소드 호출
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
}
