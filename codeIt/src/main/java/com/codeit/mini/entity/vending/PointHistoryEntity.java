package com.codeit.mini.entity.vending;

import com.codeit.mini.entity.comm.BaseEntity;
import com.codeit.mini.entity.member.MemberEntity;

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
		name = "POINT_HISTORY_ID_SEQ",
		sequenceName = "point_id_seq",
		initialValue = 1,
		allocationSize = 1
		)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "point_history")
public class PointHistoryEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POINT_HISTORY_ID_SEQ")
	@Column(name = "point_id")
	private Long pointId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private MemberEntity memberId;
	
	@Column(nullable = false)
	private Integer amount;
	
	@Column(length = 20)
	private String type;
	
	@Column(length = 50)
	private String reason;
	
}
