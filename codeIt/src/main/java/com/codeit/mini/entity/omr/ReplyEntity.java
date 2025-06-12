package com.codeit.mini.entity.omr;


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
		name = "REPLY_SEQ_GEN",
		sequenceName = "reply_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "reply")
public class ReplyEntity extends BaseEntity{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "REPLY_SEQ_GEN")
	@Column(name = "reply_id")
	private Long replyId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private MemberEntity memberId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_id", nullable = false)
	private TestEntity testId;
	
	@Column(name = "content", nullable = false, length = 500)
	private String content;
/*	
	public ReplyEntity toDto(ReplyDTO replyDto) {
		
	}
*/	
	public void changeContent(String content) {
		this.content = content;
	}
	
	
}
