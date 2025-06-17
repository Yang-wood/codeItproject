package com.codeit.mini.service.vending.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codeit.mini.dto.vending.PointHistoryDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.PointHistoryEntity;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.vending.IPointHistoryRepository;
import com.codeit.mini.service.vending.IPointHistoryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements IPointHistoryService{
	
	private final IPointHistoryRepository	pointRepository;
	private final IMemberRepository memberRepository;
	
	@Transactional
	@Override
	public PointHistoryEntity usePoint(Long memberId, int amount, String reason) {
		MemberEntity member = memberRepository.findById(memberId)
											  .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
		
		if(member.getPoints() < amount) {
			throw new IllegalArgumentException("포인트가 부족합니다.");
		}
		
		member.setPoints(member.getPoints() - amount);
		
		 PointHistoryEntity history = pointRepository.save(PointHistoryEntity.builder()
																		     .memberId(member)
																		     .type("use")
																		     .amount(-amount)
																		     .reason(reason)
																		     .build());
		return pointRepository.save(history);
	}

	@Transactional
	@Override
	public PointHistoryEntity  chargePoint(Long memberId, int amount, String reason) {
		MemberEntity member = memberRepository.findById(memberId)
											  .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
		if (amount <= 0) {
			throw new IllegalArgumentException("금액이 0보다 커야합니다.");
		}
		
		member.setPoints(member.getPoints() + amount);
		
		PointHistoryEntity history = pointRepository.save(PointHistoryEntity.builder()
											   								.memberId(member)
											   								.type("charge")
											   								.amount(amount)
											   								.reason(reason)
											   								.build());
		return pointRepository.save(history);
	}

	@Transactional
	@Override
	public PointHistoryEntity  refundPoint(Long memberId, int amount, String reason) {
		return chargePoint(memberId, amount, reason + " (환불)");
	}

	@Transactional
	@Override
	public int getCurrentPoint(Long memberId) {
		MemberEntity member = memberRepository.findById(memberId)
											  .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
		return member.getPoints();
	}
	
	@Transactional
	@Override
	public Page<PointHistoryDTO> getPointHistory(Long memberId, Pageable page) {
		return pointRepository.findByMemberId_MemberId(memberId, page).map(this::toDTO);
	}
	
}
