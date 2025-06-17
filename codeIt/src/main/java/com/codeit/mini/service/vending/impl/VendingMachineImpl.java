package com.codeit.mini.service.vending.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.vending.VendingMachineDTO;
import com.codeit.mini.dto.vending.VendingResultDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.CouponHistoryEntity;
import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.PointHistoryEntity;
import com.codeit.mini.entity.vending.VendingHistoryEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.IAdminRepository;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.vending.IMachineItemRepository;
import com.codeit.mini.repository.vending.IPointHistoryRepository;
import com.codeit.mini.repository.vending.IVendingHistoryRepository;
import com.codeit.mini.repository.vending.IVendingMachinesRepository;
import com.codeit.mini.service.vending.ICouponHistoryService;
import com.codeit.mini.service.vending.IPointHistoryService;
import com.codeit.mini.service.vending.IVendingMachineService;

import jakarta.annotation.Nullable;
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
	private final IMemberRepository memberRepository;
	private final IPointHistoryService pointService;
	private final IVendingHistoryRepository vendingHistoryRepository;
	private final ICouponHistoryService couponService;
	
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

	@Transactional
	@Override
	public VendingResultDTO useVendingMachine(Long vmId, Long memberId, @Nullable Long itemId) {
	    
		VendingMachinesEntity vendingMachine = machinesRepository.findById(vmId)
																 .orElseThrow(() -> new RuntimeException("존재하지 않는 자판기입니다."));

	    if (vendingMachine.getIsActive() == 0) {
	        throw new IllegalStateException("비활성화된 자판기입니다.");
	    }

	    MemberEntity member = memberRepository.findById(memberId)
	    									  .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
	    
	    String type = vendingMachine.getType();
	    List<MachineItemEntity> itemList = machineItemRepository.findAllByVendingMachineWithLock(vmId);
        
	    if (itemList.isEmpty()) {
            throw new IllegalStateException("자판기에 등록된 아이템이 없습니다.");
        }
	    
	    List<MachineItemEntity> filtered = itemList.stream()
	            								   .filter(mi -> mi.getVendingItem().getIsActive() == 1 && mi.getVendingItem().getStock() > 0)
	            								   .toList();
	    
	    if (filtered.isEmpty()) {
	        throw new IllegalStateException("사용 가능한 아이템이 없습니다.");
	    }
	    
	    boolean isFreePassUsed = false;
	    
	    MachineItemEntity selected;
	    if ("choice".equalsIgnoreCase(type)) {
	        if (itemId == null) {
	            throw new IllegalArgumentException("선택 자판기에는 itemId가 필요합니다.");
	        }

	        selected = itemList.stream().filter(mi -> mi.getVendingItem().getItemId().equals(itemId))
							            .findFirst()
							            .orElseThrow(() -> new RuntimeException("해당 아이템은 자판기에 없습니다."));
	        
	    } else if ("random".equalsIgnoreCase(type)) {
	    	 Optional<MachineItemEntity> freeItem = filtered.stream()
	    	            									.filter(mi -> "free".equalsIgnoreCase(mi.getVendingItem().getItemType()))
	    	            									.findFirst();
	    	 
	    	 if (freeItem.isPresent()) {
	             selected = freeItem.get();
	             isFreePassUsed = true;
	         } else {
	             selected = drawRandomItem(filtered);
	         }
	    } else {
	        throw new IllegalArgumentException("알 수 없는 자판기 타입입니다: " + type);
	    }

	    VendingItemEntity item = selected.getVendingItem();
	    
	    if (item.getValue() < 0) {
	        throw new IllegalStateException("아이템 가격은 0P 이상이어야 합니다.");
	    }
	    
	    if (item.getStock() <= 0) {
	        throw new IllegalStateException("재고 부족으로 자판기 이용이 불가합니다.");
	    }
	    item.setStock(item.getStock() - 1);
	    
	    PointHistoryEntity pointHistory = null;
	    CouponHistoryEntity coupon = null;

	    if (!isFreePassUsed && item.getValue() > 0) {
	    	pointHistory = pointService.usePoint(memberId, item.getValue(), "자판기 이용 - " + item.getName());
	    }
	    
	    if ("point".equalsIgnoreCase(item.getItemType())) {
	        pointService.chargePoint(memberId, item.getValue(), "자판기 보상 - " + item.getName());

	    } else if (!"free".equalsIgnoreCase(item.getItemType())) {
	        coupon = couponService.issueCoupon(memberId, item.getItemId(), vmId);
	    }
	    
	    vendingHistoryRepository.save(VendingHistoryEntity.builder()
	            										  .memberId(member)
	            										  .itemId(item)
	            										  .pointId(pointHistory)
	            										  .couponId(coupon)
	            										  .payment(isFreePassUsed ? "free" : "point")
	            										  .status("success")
	            										  .build());

	    return VendingResultDTO.builder().itemName(item.getName())
	            						 .couponCode(coupon != null ? coupon.getCouponCode() : null)
	            						 .costPoint(isFreePassUsed ? 0 : item.getValue())
	            						 .rewardPoint("point".equals(item.getItemType()) ? item.getValue() : 0)
	            						 .message("point".equals(item.getItemType()) ? item.getValue() + "P가 지급되었습니다!" : (coupon != null ? "'" + item.getName() + "' 쿠폰이 발급되었습니다!" : "'" + item.getName() + "'이(가) 사용되었습니다."))
	            						 .build();
	}

	private MachineItemEntity drawRandomItem(List<MachineItemEntity> itemList) {
		double totalWeight = itemList.stream()
                					 .mapToDouble(mi -> Optional.ofNullable(mi.getProbability()).orElse(1.0))
                					 .sum();

		double rand = Math.random() * totalWeight;
	    double cumulative = 0.0;
	    
	    for (MachineItemEntity mi : itemList) {
	        double weight = Optional.ofNullable(mi.getProbability()).orElse(1.0);
	        cumulative += weight;
	        
	        if (rand <= cumulative) {
	            return mi;
	        }
	    }
	    
	    log.warn("[자판기] 확률 로직 이상 감지 - fallback item 사용. rand={}, totalWeight={}, listSize={}", rand, totalWeight, itemList.size());
		return itemList.get(itemList.size() - 1);
	}
	
}
