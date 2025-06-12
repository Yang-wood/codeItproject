package com.codeit.mini.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass	// 공통 필드나 매핑 정보를 상속을 통해 재사용할 때 사용
@EntityListeners(value = {AuditingEntityListener.class})	// JPA 엔티티의 생명주기 이벤트(등록, 수정 등)에 대한 콜백 메서드를 지정된 클래스에서 실행하도록 설정
@Getter
public abstract class BaseEntity {
	
	@CreatedDate	// 엔티티가 처음 저장(persist) 될 때 자동으로 날짜/시간을 채워주는 용도
	@Column(name = "regdate", updatable = false)
	private LocalDateTime regDate;
	
	@LastModifiedDate	// 엔티티가 수정(Update) 될 때 자동으로 날짜/시간을 기록
	@Column(name = "updatedate")
	private LocalDateTime updateDate;
}
