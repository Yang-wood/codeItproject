package com.codeit.mini.service.vending.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.vending.TestCouponDTO;
import com.codeit.mini.dto.vending.VendingMachineDTO;
import com.codeit.mini.dto.vending.VendingResultDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.comm.VendingType;
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
import com.codeit.mini.repository.vending.IVendingItemRepository;
import com.codeit.mini.repository.vending.IVendingMachinesRepository;
import com.codeit.mini.repository.vending.search.IVendingMachinesSearch;
import com.codeit.mini.service.vending.ICouponHistoryService;
import com.codeit.mini.service.vending.IPointHistoryService;
import com.codeit.mini.service.vending.ITestCouponService;
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
	private final IVendingItemRepository itemRepository;
	private final IMemberRepository memberRepository;
	private final IPointHistoryService pointService;
	private final IVendingHistoryRepository vendingHistoryRepository;
	private final ICouponHistoryService couponService;
	private final ITestCouponService testCouponService;
	private final IVendingMachinesSearch machinesSearch;
	private final IPointHistoryRepository pointHistoryRepository;
	
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
		
	    Admin admin = null;
	    if (vmDto.getAdminId() != null) {
	        admin = adminRepository.findById(vmDto.getAdminId())
	                               .orElseThrow(() -> new IllegalArgumentException("관리자 정보가 없습니다. 관리자 번호 = " + vmDto.getAdminId()));
	    }
		
		VendingMachinesEntity vmEntity = toEntity(vmDto, admin);
		
		machinesRepository.save(vmEntity);
		
		return vmEntity.getMachineId();
	}

	@Override
	public Optional<VendingMachineDTO> findVendingMachineById(Long vmId) {
		Optional<VendingMachinesEntity> optionalVm = machinesRepository.findByMachineIdAndIsActive(vmId, 1);

	    if (optionalVm.isEmpty()) {
	        return Optional.empty();
	    }

	    VendingMachinesEntity vm = optionalVm.get();

	    List<MachineItemEntity> machineItems = machineItemRepository.findByVendingMachine_MachineId(vmId);

	    VendingMachineDTO dto = toDetail(vm, machineItems);

	    return Optional.of(dto);
	}

	@Override
	public List<VendingMachineDTO> findAllVendingMachineById(Long vmId) {
		
		List<VendingMachinesEntity> vmEntityList = machinesRepository.findAll();
		List<VendingMachineDTO> vmDtoList = vmEntityList.stream().map(vmEntity -> VendingMachineDTO.builder()
																								   .machineId(vmEntity.getMachineId())
																								   .adminId(vmEntity.getAdminId() == null ? null : vmEntity.getAdminId().getAdminId())
																								   .name(vmEntity.getName())
																								   .type(vmEntity.getType().name())
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
	
	@Override
	public PageResultDTO<VendingMachineDTO, VendingMachinesEntity> getVendingMachines(PageRequestDTO requestDTO) {
		Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());

	    Page<VendingMachinesEntity> result = machinesRepository.findByIsActive(1, pageable);

	    return new PageResultDTO<>(result, this::toDTO);
	}
	
	@Override
	public PageResultDTO<VendingMachineDTO, VendingMachinesEntity> getVendingMachinesWithFilter(PageRequestDTO requestDTO) {
		Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());
		
		Page<VendingMachinesEntity> result = machinesSearch.searchMachines(requestDTO, pageable);
	    return new PageResultDTO<>(result, this::toDTO);
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
	    
	    VendingType type = vendingMachine.getType();
	    
	    List<MachineItemEntity> itemList = machineItemRepository.findAllByVendingMachineWithLock(vmId);
	    if (itemList.isEmpty()) {
	        throw new IllegalStateException("자판기에 등록된 아이템이 없습니다.");
	    }
	    
	    List<MachineItemEntity> filtered = itemList.stream().filter(mi -> {
	                														VendingItemEntity item = mi.getVendingItem();
	                														return item.getIsActive() == 1 && (item.getStock() == null || item.getStock() > 0);
	            														  }).toList();

	    if (filtered.isEmpty()) {
	    	throw new IllegalStateException("사용 가능한 아이템이 없습니다.");
	    }
	    
	    MachineItemEntity selected;
	    boolean isFreePassUsed = false;
	    TestCouponDTO usedFreePass = null;
	    
	    switch (type) {
	        case CHOICE -> {
		    	if (itemId == null) {
		    		throw new IllegalArgumentException("선택 자판기에는 itemId가 필요합니다.");
		    	}
		    	selected = itemList.stream().filter(mi -> mi.getVendingItem().getItemId().equals(itemId))
		                					.findFirst()
		                					.orElseThrow(() -> new RuntimeException("해당 아이템은 자판기에 없습니다."));
		    }
	        case RANDOM -> {
	        	Optional<TestCouponDTO> passOpt = testCouponService.useFreePassIfExists(memberId);
	        	if (passOpt.isPresent()) {
	        		isFreePassUsed = true;
	        		usedFreePass = passOpt.get();
	        	}
	        	selected = drawRandomItem(filtered);
	        }
	        default -> throw new IllegalArgumentException("알 수 없는 자판기 타입입니다: " + type);
	    }
	    
	    VendingItemEntity item = selected.getVendingItem();
	    
	    if (item.getValue() < 0) {
	        throw new IllegalStateException("아이템 가격은 0P 이상이어야 합니다.");
	    }
	    
	    if (item.getStock() != null && item.getStock() <= 0) {
	        throw new IllegalStateException("재고 부족으로 자판기 이용이 불가합니다.");
	    }
	    
	    if (item.getStock() != null) {
	        item.setStock(item.getStock() - 1);
	    }
	    
	    int costPoint = 0;
	    PointHistoryEntity pointHistory = null;
	    CouponHistoryEntity coupon = null;

	    if (!isFreePassUsed) {
	    	costPoint = (type == VendingType.RANDOM) ? 100 : item.getValue();
	        if (costPoint > 0) {
	            pointHistory = pointService.usePoint(memberId, costPoint, "자판기 이용 - " + item.getName());
	        }
	    }
	    log.info("📦 itemId: {}, 이름: {}, 타입: '{}'", item.getItemId(), item.getName(), item.getItemType());

	    if ("point".equalsIgnoreCase(item.getItemType())) {
	        pointService.chargePoint(memberId, item.getValue(), "자판기 보상 - " + item.getName());
	    
	    } else if (!"free".equalsIgnoreCase(item.getItemType())) {
	        coupon = couponService.issueCoupon(memberId, item.getItemId(), vmId);
	    }
	    
	    String itemType = Optional.ofNullable(item.getItemType()).orElse("").trim().toLowerCase();
	    if ("test".equals(itemType) || "free".equals(itemType)) {
	        testCouponService.issueTestCoupon(memberId, item.getItemId(), vmId);
	    }
	    
	    vendingHistoryRepository.save(VendingHistoryEntity.builder()
														  .memberId(member)
														  .itemId(item)
														  .pointId(pointHistory)
														  .couponId(coupon)
														  .payment("point")
														  .status("success")
														  .build());
	    
	    String message;
	    
	    if ("point".equalsIgnoreCase(item.getItemType())) {
	        message = item.getValue() + "P가 지급되었습니다!";
	    } else if (coupon != null) {
	        message = "'" + item.getName() + "' 쿠폰이 발급되었습니다!";
	    } else if (isFreePassUsed) {
	        message = "무료 뽑기권이 사용되었습니다!";
	    } else {
	        message = "'" + item.getName() + "'이(가) 사용되었습니다.";
	    }

	    return VendingResultDTO.builder()
							   .itemName(item.getName())
							   .couponCode(coupon != null ? coupon.getCouponCode() : null)
							   .costPoint(isFreePassUsed ? 0 : costPoint)
							   .rewardPoint("point".equals(item.getItemType()) ? item.getValue() : 0)
							   .message(message)
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

	@Override
	public VendingResultDTO purchaseMultipleItems(Long vmId, Long memberId, List<Long> itemIds) {
		 log.info("📦 [SERVICE] 결제 시작 - machineId: {}, memberId: {}, itemIds: {}", vmId, memberId, itemIds);
		MemberEntity member = memberRepository.findById(memberId)
	            .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

	        VendingMachinesEntity machine = machinesRepository.findById(vmId)
	            .orElseThrow(() -> new RuntimeException("자판기가 존재하지 않습니다."));

	        List<VendingItemEntity> items = itemRepository.findAllById(itemIds);

	        int totalCost = items.stream().mapToInt(VendingItemEntity::getValue).sum();

	        if (member.getPoints() < totalCost) {
	            throw new IllegalStateException("포인트가 부족합니다.");
	        }

	        member.setPoints(member.getPoints() - totalCost);
	        memberRepository.save(member);

	        pointHistoryRepository.save(PointHistoryEntity.builder()
	            .memberId(member)
	            .amount(-totalCost)
	            .type("USE")
	            .reason("선택형 자판기 구매")
	            .build());

	        for (VendingItemEntity item : items) {
	        	String type = Optional.ofNullable(item.getItemType()).orElse("").toLowerCase();
	        	if ("test".equals(type) || "free".equals(type)) {
	                testCouponService.issueTestCoupon(memberId, item.getItemId(), vmId);
	            } else if (!"point".equals(type)) {
	                couponService.issueCoupon(memberId, item.getItemId(), vmId);
	            }
	            vendingHistoryRepository.save(VendingHistoryEntity.builder()
	                .memberId(member)
	                .itemId(item)
	                .status("SUCCESS")
	                .build());
	        }
	        log.info("✅ [SERVICE] 결제 완료 - 리턴 준비");
	        return VendingResultDTO.builder()
	            .message("결제가 완료되었습니다.")
	            .costPoint(totalCost)
	            .itemList(items.stream().map(VendingItemEntity::getName).collect(Collectors.toList()))
	            .build();
	}
	
}
