package com.codeit.mini.entity.book;

import java.time.LocalDateTime;

import com.codeit.mini.entity.member.MemberEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Builder
@Entity
@SequenceGenerator(
			name = "WISH_SEQ_GEN",
			sequenceName = "wish_seq",
			initialValue = 1,
			allocationSize = 1
		)
@Table(name = "wish")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WishEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WISH_SEQ_GEN")
	private Long wishId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity memberEntity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private BookEntity bookEntity;
	
	@Column(name = "regdate", nullable = false)
	private LocalDateTime regDate;
	
	@PrePersist
    public void prePersist() {
        if (regDate == null) {
        	regDate = LocalDateTime.now();
        }
    }
}
