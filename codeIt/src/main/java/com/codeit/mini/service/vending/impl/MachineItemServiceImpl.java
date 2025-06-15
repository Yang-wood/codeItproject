package com.codeit.mini.service.vending.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.vending.MachineItemDTO;
import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.MachineItemId;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.vending.IMachineItemRepository;
import com.codeit.mini.repository.vending.IVendingItemRepository;
import com.codeit.mini.repository.vending.IVendingMachinesRepository;
import com.codeit.mini.service.vending.IMachineItemService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class MachineItemServiceImpl implements IMachineItemService{

	private final IMachineItemRepository repository;
	
	private final IVendingMachinesRepository vmRepository;
	
	private final IVendingItemRepository itemRepository;

	@Override
	public Long registerMachineItem(MachineItemDTO dto) {
		
		VendingMachinesEntity machineEntity = vmRepository.findById(dto.getMachineId())
														  .orElseThrow(() -> new IllegalArgumentException("해당 자판기 없음"));
		
		VendingItemEntity itemEntity =  itemRepository.findById(dto.getItemId())
													  .orElseThrow(() -> new IllegalArgumentException("해당 아이템 없음"));
		
		MachineItemId id = new MachineItemId(dto.getMachineId(), dto.getItemId());
		
		if (repository.existsById(id)) {
            throw new IllegalStateException("이미 연결된 아이템입니다.");
        }
		
		MachineItemEntity machineItem = new MachineItemEntity();
		machineItem.setId(id);
		machineItem.setVendingMachine(machineEntity);
		machineItem.setVendingItem(itemEntity);
		machineItem.setProbability(dto.getProbability());
		
		MachineItemEntity save = repository.save(machineItem);
		
		return save.getId().getMachineId();
	}
	
	@Override
	@Transactional
	public List<Long> registerMachineItems(List<MachineItemDTO> dtoList) {
		List<Long> vmIdList = new ArrayList<>();
		
		for (MachineItemDTO dto : dtoList) {
			registerMachineItem(dto);
			vmIdList.add(dto.getMachineId());
		}
			
		return vmIdList;
	}

	@Override
	public Optional<MachineItemDTO> findMachineItemById(Long machineId, Long itemId) {
		
		MachineItemId id = new MachineItemId(machineId, itemId);
		Optional<MachineItemEntity> getReadId = repository.findById(id);
		
		return getReadId.map(this::toDTO);
		
//		Optional<T> 타입에서 값이 있을 때만 함수(entitiesToDto)를 적용해서 다른 타입으로 변환해주는 메서드
//		Optional<MachineItemDTO> dtoOpt;
//		if(getReadId.isPresent()) {
//		    dtoOpt = Optional.of(entitiesToDto(getReadId.get()));
//		} else {
//		    dtoOpt = Optional.empty();
//		}
//		return dtoOpt;
	}
	
	@Override
	public List<MachineItemDTO> findAllItemsByMachineId(Long machineId) {
		
		List<MachineItemEntity> vmItems = repository.findByVendingMachine_MachineId(machineId);
		
		List<MachineItemDTO> itemList = vmItems.stream().map(this::toDTO)
														.collect(Collectors.toList());
		
		return itemList;
	}

	@Override
	@Transactional
	public MachineItemDTO updateMachineItem(MachineItemDTO dto) {
		
		MachineItemId id = new MachineItemId(dto.getMachineId(), dto.getItemId());
		
		MachineItemEntity entity = repository.findById(id)
											 .orElseThrow(() -> new IllegalArgumentException("연결되어 있지 않음"));
		
		entity.setProbability(dto.getProbability());
		repository.save(entity);
		
		return toDTO(entity);
	}
	
	@Transactional
	@Override
	public void changeDeactiveMachine(Long machineId) {
		
		if (!vmRepository.existsById(machineId)) {
			throw new IllegalArgumentException("존재하지 않는 자판기입니다: " + machineId);
		}
		
		vmRepository.deactivateMachine(machineId);
	}
	
	@Transactional
	@Override
	public void changeActivateMachine(Long machineId) {
	    VendingMachinesEntity machine = vmRepository.findById(machineId)
	    											.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 자판기입니다."));
	    machine.setIsActive(1); // 다시 활성화
	}
	
	@Transactional
	@Override
	public boolean removeMachineItemId(Long machineId, Long itemId) {
		
		MachineItemId id = new MachineItemId(machineId, itemId);
		
		repository.deleteById(id);
		
		return !repository.existsById(id);
	}
	
	@Transactional
	@Override
	public boolean removeVendingItem(Long itemId) {
		if (!itemRepository.existsById(itemId)) {
			return false;
		}
		repository.deleteByItemId(itemId);
		
		return true;
	}
	
	@Override
	@Transactional
	public void removeAllByMachineId(Long machineId) {
		
	    repository.deleteByMachineId(machineId);
	}
	
}
