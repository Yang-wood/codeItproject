package com.codeit.mini.service.vending;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codeit.mini.dto.vending.PointHistoryDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.PointHistoryEntity;

public interface IPointHistoryService {

//	포인트 사용 -> 자판기
//	포인트 충전
//	포인트 이력 조회
//	포인트 환불
	
    public PointHistoryEntity usePoint(Long memberId, int amount, String reason);

    public PointHistoryEntity chargePoint(Long memberId, int amount, String reason);

    public PointHistoryEntity refundPoint(Long memberId, int amount, String reason);

    public int getCurrentPoint(Long memberId);

    Page<PointHistoryDTO> getPointHistory(Long memberId, Pageable page);
	
    
    default PointHistoryDTO toDTO (PointHistoryEntity entity) {
    	PointHistoryDTO dto = PointHistoryDTO.builder()
    										 .memberId(entity.getMemberId().getMemberId())
    										 .amount(entity.getAmount())
    										 .reason(entity.getReason())
    										 .type(entity.getType())
    										 .regDate(entity.getRegDate())
    										 .build();
    	return dto;
    }
    
    default PointHistoryEntity entity (PointHistoryDTO dto, MemberEntity mEntity) {
    	PointHistoryEntity entity = PointHistoryEntity.builder()
    												  .memberId(mEntity)
    												  .amount(dto.getAmount())
    												  .reason(dto.getReason())
    												  .type(dto.getType())
    												  .build();
    	return entity;
    }
}
