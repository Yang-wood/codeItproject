package com.codeit.mini.entity.comm;

import java.time.LocalDateTime;

import org.hibernate.annotations.processing.Pattern;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.CouponHistoryEntity;
import com.codeit.mini.entity.vending.PointHistoryEntity;
import com.codeit.mini.entity.vending.TestCouponEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(value = AuditingEntityListener.class)
@SequenceGenerator(
		name = "PAYMENT_SEQ_GEN",
		sequenceName = "payment_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "payment")
public class PaymentEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "PAYMENT_SEQ_GEN")
	private Long paymentId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private MemberEntity memberId;
	
	@Column(nullable = false)
	private String paymentType;
	
	//      비즈니스 로직에서 PaymentEntity.getPaymentType().equals("sessionId 또는 rentId")
	//		if문 활용해서 test / book / vending 인지 확인
	@Column(name = "target_id", nullable = false)
	private Long targetId;
	
	private String method;
	
	@Column(length = 30)
	private String status;
	
	private String couponType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id")
	private CouponHistoryEntity couponId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_coupon_id")
	private TestCouponEntity testCouponId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "point_id")
	private PointHistoryEntity pointId;
	
//	사용되는 수량 또는 결제 값
	private int amount;
	
	@CreatedDate
	@Column(name = "regdate")
	private LocalDateTime regDate;
}
