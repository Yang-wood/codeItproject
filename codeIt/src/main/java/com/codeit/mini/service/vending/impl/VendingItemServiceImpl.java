package com.codeit.mini.service.vending.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.vending.VendingItemDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.repository.IAdminRepository;
import com.codeit.mini.repository.vending.IMachineItemRepository;
import com.codeit.mini.repository.vending.IVendingItemRepository;
import com.codeit.mini.service.vending.IVendingItemService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class VendingItemServiceImpl implements IVendingItemService{

	private final IVendingItemRepository itemRepository;
	private final IAdminRepository adminRepository;
	private final IMachineItemRepository vmItemRepository;

	@Override
	@Transactional
	public Long registerVendingItem(VendingItemDTO itemDto) {
		
		Admin admin = adminRepository.findById(itemDto.getAdminId())
									 .orElseThrow(() -> new IllegalArgumentException("admin이 존재 하지 않습니다."));
		
		if(itemDto.getName() == null || itemDto.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("상품명은 필수입니다.");
		}
		
		Optional<VendingItemEntity> findItemName = itemRepository.findByName(itemDto.getName());
		
		if(findItemName.isPresent()) {
			throw new IllegalArgumentException(itemDto.getName() + "라는 이름은 이미 존재하는 상품명입니다.");
		}
		
		if(itemDto.getProbability() < 0) {
			throw new IllegalArgumentException("확률은 0 이상이어야 합니다.");
		}
		
		VendingItemEntity itemEntity = toEntity(itemDto, admin);
		VendingItemEntity savedEntity = itemRepository.save(itemEntity);
		
		return savedEntity.getItemId();
	}
	
	@Transactional
	@Override
	public List<Long> registerItems(List<VendingItemDTO> itemsDto) {
		List<Long> saveIds = new ArrayList<>();
		
		for (VendingItemDTO dto : itemsDto) {
			
			if(dto.getProbability() < 0) {
				throw new IllegalArgumentException("확률은 0 이상이어야 합니다.");
			}
			
			if(dto.getName() == null || dto.getName().trim().isEmpty()) {
				throw new IllegalArgumentException("상품명은 필수입니다.");
			}
			
		    if(itemRepository.findByName(dto.getName()).isPresent()) {
		        throw new IllegalArgumentException(dto.getName() + " 이미 존재하는 상품명입니다.");
		    }
		    
		    Admin admin = adminRepository.findById(dto.getAdminId())
                    								  .orElseThrow(() -> new IllegalArgumentException("관리자가 없습니다."));
		    
			VendingItemEntity entity = toEntity(dto, admin);
			
			saveIds.add(itemRepository.save(entity).getItemId());
		}
		
		return saveIds;
	}

	@Override
	public Optional<VendingItemDTO> findVendingItemById(Long itemId) {
		
		Optional<VendingItemEntity> itemEntity = itemRepository.findById(itemId);
		
		if (itemEntity.isEmpty()) {
			throw new IllegalArgumentException(itemId + "번 상품은 존재하지 않습니다.");
		}
		
		VendingItemDTO itemDto = toDTO(itemEntity.get());
		
		return Optional.of(itemDto);
	}

	@Override
	public List<VendingItemDTO> findAllVendingItemById() {
		
		List<VendingItemEntity> itemEntityList = itemRepository.findAll();
		List<VendingItemDTO> itemDtoList = itemEntityList.stream().map(itemEntity -> VendingItemDTO.builder()
																								   .itemId(itemEntity.getItemId())
																								   .adminId(itemEntity.getAdminId().getAdminId())
																								   .name(itemEntity.getName())
																								   .itemType(itemEntity.getItemType())
																								   .probability(itemEntity.getProbability())
																								   .active(itemEntity.getIsActive())
																								   .regDate(itemEntity.getRegDate())
																								   .build()).collect(Collectors.toList());
		
		return itemDtoList;
	}

	@Override
	@Transactional
	public VendingItemDTO updateVendingItem(VendingItemDTO itemDto) {
		
		Optional<VendingItemEntity> itemEntity = itemRepository.findById(itemDto.getItemId());
		
		if(itemEntity.isEmpty()) {
			throw new IllegalArgumentException(itemDto.getItemId() + "번 상품은 존재하지 않습니다.");
		}
		
		if(itemDto.getProbability() < 0) {
			throw new IllegalArgumentException("확률은 0 이상이어야 합니다.");
		}
		
		VendingItemEntity item = itemEntity.get();
		
		item.setName(itemDto.getName());
		item.setDescription(itemDto.getDescription());
		item.setProbability(itemDto.getProbability());
		item.setValue(itemDto.getValue());
		item.setStock(itemDto.getStock());
		item.setItemType(itemDto.getItemType());
		item.setIsActive(itemDto.getActive());
		
		VendingItemEntity saveItem =  itemRepository.save(item);
		
		return toDTO(saveItem);
	}

	@Override
	@Transactional
	public boolean changeActiveItem(Long itemId) {
		
		Optional<VendingItemEntity> targetItem = itemRepository.findById(itemId);
		
		if (targetItem.isEmpty()) {
			throw new IllegalArgumentException(itemId + "번 상품은 존재하지 않습니다.");
		}
		
	    VendingItemEntity entity = targetItem.get();
	    entity.setIsActive(0);
	    
	    itemRepository.save(entity);
		
		return true;
	}
	
	@Override
	@Transactional
	public boolean changeActiveItems(List<Long> itemIds) {
		
		if (itemIds == null || itemIds.isEmpty()) throw new IllegalArgumentException("선택된 아이템이 없습니다.");
		
		int updated = itemRepository.deactivateByIds(itemIds);
		
		return updated > 0;
	}

	@Override
	@Transactional
	public boolean removeVendingItem(Long itemId) {
		
		Optional<VendingItemEntity> targetItem = itemRepository.findById(itemId);
		
		if (targetItem.isEmpty()) {
			throw new IllegalArgumentException(itemId + "번 상품은 존재하지 않습니다.");
		}
		
	    if (!itemRepository.existsById(itemId)) {
	        return false;
	    }
	    
	    vmItemRepository.deleteByItemId(itemId);
		
		itemRepository.deleteById(itemId);
		
		return true;
	}

	@Override
	@Transactional
	public boolean removeVendingItems(List<Long> itemIds) {
		for (Long itemId : itemIds) {
	        vmItemRepository.deleteByItemId(itemId);
	    }
		
		itemRepository.deleteAllByIdInBatch(itemIds);
		
		return true;
	}
	
}
