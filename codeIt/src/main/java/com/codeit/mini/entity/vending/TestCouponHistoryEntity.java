package com.codeit.mini.entity.vending;

import java.time.LocalDateTime;

import com.codeit.mini.entity.comm.BaseEntity;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@SequenceGenerator(
		name = "TEST_COUPON_ID_SEQ",
		sequenceName = "test_coupon_id_seq",
		initialValue = 1,
		allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"memberId", "testCouponId"})
@Table(name = "test_coupon_history")
public class TestCouponHistoryEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_COUPON_ID_SEQ")
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_coupon_id", nullable = false, foreignKey = @ForeignKey(name = "fk_test_coupon_id_history"))
    private TestCouponEntity testCouponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_member_id_test_coupon_history"))
    private MemberEntity memberId;
    
    @Column(name = "coupon_code", nullable = false)
    private String couponCode;
    
    @Builder.Default
    @Column(name = "status", nullable = false)
	private String status = "issued";

    @Column(name = "description", length = 300)
    private String description;
    
	@Column(name = "used_date")
	private LocalDateTime usedDate;
	
	@Column(name = "expire_date")
	private LocalDateTime expireDate;
}
