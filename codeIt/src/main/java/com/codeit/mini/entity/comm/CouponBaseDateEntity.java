package com.codeit.mini.entity.comm;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
public class CouponBaseDateEntity {

	@CreatedDate
	@Column(name = "issued_date", updatable = false)
	private LocalDateTime issuedDate;
	
	@Column(name = "used_date")
	private LocalDateTime usedDate;
	
	@Column(name = "expire_date")
	private LocalDateTime expireDate;
}
