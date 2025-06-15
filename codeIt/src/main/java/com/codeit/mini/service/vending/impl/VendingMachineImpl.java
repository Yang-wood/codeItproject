package com.codeit.mini.service.vending.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.vending.VendingMachineDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.IAdminRepository;
import com.codeit.mini.repository.vending.IMachineItemRepository;
import com.codeit.mini.repository.vending.IVendingMachinesRepository;
import com.codeit.mini.service.vending.IVendingMachineService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class VendingMachineImpl implements IVendingMachineService{
	
	private final IVendingMachinesRepository machinesRepository;
	private final IAdminRepository adminRepository;
	private final IMachineItemRepository machineItemRepository;
	
	@Override
	@Transactional
	public Long registerVendingMachine(VendingMachineDTO vmDto) {
		
		Optional<VendingMachinesEntity> findVmName = machinesRepository.findByName(vmDto.getName());
		
		if(vmDto.getName() == null || vmDto.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("자판기 이름은 필수입니다.");
		}
		
		if(vmDto.getType() == null || vmDto.getType().trim().isEmpty()) {
			throw new IllegalArgumentException("자판기 타입은 필수입니다.");
		}
		
		if(findVmName.isPresent()) {
			throw new IllegalArgumentException(vmDto.getName() + "라는 이름은 이미 존재하는 자판기 이름입니다.");
		}
		
		VendingMachinesEntity vmEntity = toEntity(vmDto);
		
		machinesRepository.save(vmEntity);
		
		return vmEntity.getMachineId();
	}

	@Override
	public Optional<VendingMachineDTO> findVendingMachineById(Long vmId) {
		
//		VendingMachinesEntity readVm = machinesRepository.findById(vmId)
//														 .orElseThrow(() -> new IllegalArgumentException("해당 자판기 정보가 없습니다. 자판기 번호 = " +vmId));
		
		return machinesRepository.findById(vmId).map(this::toDTO);
		
//		return Optional.of(entitiesToDto(readVm));
	}

	@Override
	public List<VendingMachineDTO> findAllVendingMachineById(Long vmId) {
		
		List<VendingMachinesEntity> vmEntityList = machinesRepository.findAll();
		List<VendingMachineDTO> vmDtoList = vmEntityList.stream().map(vmEntity -> VendingMachineDTO.builder()
																								   .machineId(vmEntity.getMachineId())
																								   .adminId(vmEntity.getAdminId() == null ? null : vmEntity.getAdminId().getAdminId())
																								   .name(vmEntity.getName())
																								   .type(vmEntity.getType())
																								   .description(vmEntity.getDescription())
																								   .active(vmEntity.getIsActive())
																								   .regDate(vmEntity.getRegDate())
																								   .build()).collect(Collectors.toList());
		return vmDtoList;
	}

	@Transactional
	@Override
	public VendingMachineDTO updateVendingMachine(VendingMachineDTO vmDto) {
		
		VendingMachinesEntity vm = machinesRepository.findById(vmDto.getMachineId())
													 .orElseThrow(() -> new IllegalArgumentException("해당 자판기 정보가 없습니다. 자판기 번호 = " + vmDto.getMachineId()));
		
		Optional<VendingMachinesEntity> vmName = machinesRepository.findByName(vmDto.getName());
		
		if (vmName.isPresent() && !vmName.get().getMachineId().equals(vmDto.getMachineId())) {
			throw new IllegalArgumentException("이미 사용중인 자판기 이름입니다.");
		}
		
		vm.changeName(vmDto.getName());
		vm.changeDesc(vmDto.getDescription());
		vm.changeType(vmDto.getType());
		vm.changeActive(vmDto.getActive());
		
		if (vmDto.getAdminId() != null) {
			Admin admin = adminRepository.findById(vmDto.getAdminId())
										 .orElseThrow(() -> new IllegalArgumentException("해당 관리자 정보가 없습니다. 관리자 번호 = " + vmDto.getAdminId()));
			vm.setAdminId(admin);
		}
		
		machinesRepository.save(vm);
		
		return toDTO(vm);
	}
	
	@Transactional
	@Override
	public boolean changeActiveVendingMahchine(Long vmId) {
		
		VendingMachinesEntity vm = machinesRepository.findById(vmId)
													 .orElseThrow(() -> new RuntimeException("자판기 없음"));
		
		machineItemRepository.deleteByVendingMachine_MachineId(vmId);
	    
	    vm.changeActive(0);
	    machinesRepository.save(vm);
	    
	    return true;
	}
	
	@Transactional
	@Override
	public boolean removeVendingMachine(Long vmId) {
		
		Optional<VendingMachinesEntity> targetVm = machinesRepository.findById(vmId);
		
		if (targetVm.isEmpty()) {
			return false;
		}
		
		machineItemRepository.deleteByVendingMachine_MachineId(vmId);
		machinesRepository.deleteById(vmId);
		
		return true;
	}

	
}
