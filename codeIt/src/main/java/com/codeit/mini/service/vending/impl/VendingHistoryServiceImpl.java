package com.codeit.mini.service.vending.impl;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.vending.VendingHistoryDTO;
import com.codeit.mini.entity.vending.VendingHistoryEntity;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.vending.IVendingHistoryRepository;
import com.codeit.mini.repository.vending.IVendingMachinesRepository;
import com.codeit.mini.service.vending.IVendingHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendingHistoryServiceImpl implements IVendingHistoryService{
	
	private final IVendingHistoryRepository historyRepository;
	private final IMemberRepository memberRepository;
	private final IVendingMachinesRepository machinesRepository;
	
	@Override
	public PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> getAllHistories(PageRequestDTO requestDTO) {
		Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());
		    
		Page<VendingHistoryEntity> result = historyRepository.findAll(pageable);
		    
		Function<VendingHistoryEntity, VendingHistoryDTO> fn = (dto -> toDTO(dto));

		return new PageResultDTO<>(result, fn);
	}
	
	@Override
	public PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> getHistoriesByMember(Long memberId, PageRequestDTO requestDTO) {
		Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());
		
		Page<VendingHistoryEntity> result = historyRepository.findByMemberId_MemberId(memberId, pageable);
		
		Function<VendingHistoryEntity, VendingHistoryDTO> fn = (dto -> toDTO(dto));
		
		return new PageResultDTO<>(result, fn);
	}
	
	@Override
	public  PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> getHistoriesByMachine(Long machineId, PageRequestDTO requestDTO) {
		Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());
		
		Page<VendingHistoryEntity> result = historyRepository.findByItemId_VendingMachine_MachineId(machineId, pageable);

		Function<VendingHistoryEntity, VendingHistoryDTO> fn = (dto -> toDTO(dto));
		
		return new PageResultDTO<>(result, fn);
	}
	
	@Override
	public long countByMember(Long memberId) {
		if (!memberRepository.existsById(memberId)) {
			throw new IllegalArgumentException("존재하지 않는 회원입니다.");
		}
		
		return historyRepository.countByMember(memberId);
	}
	
	@Override
	public long countByMachine(Long machineId) {
		if (!machinesRepository.existsById(machineId)) {
		    throw new IllegalArgumentException("존재하지 않는 자판기입니다.");
		}
		
		 return historyRepository.countByMachine(machineId);
	}
	
	@Override
	public int getTotalUsedPoints(Long memberId) {
		return historyRepository.sumUsedPointsByMember(memberId);
	}
	
	@Override
	public int getTotalRewardedPoints(Long memberId) {
		return historyRepository.sumRewardedPointsByMember(memberId);
	}
	
	@Override
	public PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> searchHistories(Long memberId, Long machineId, String payment, String status, PageRequestDTO requestDTO) {
		Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());

		Page<VendingHistoryEntity> result = historyRepository.searchHistoriesWithFilters(memberId, machineId, payment, status, pageable);
	    
		Function<VendingHistoryEntity, VendingHistoryDTO> fn = (dto -> toDTO(dto));;
		
	    return new PageResultDTO<>(result, fn);
	}

}
