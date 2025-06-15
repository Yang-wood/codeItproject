package com.codeit.mini.service.vending;

import java.util.List;
import java.util.Optional;
import static com.codeit.mini.service.vending.UtilDefault.defaultIfNull;

import com.codeit.mini.dto.vending.VendingItemDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.vending.VendingItemEntity;

public interface IVendingItemService {
	
	Long registerVendingItem(VendingItemDTO itemDto);
	
	List<Long> registerItems(List<VendingItemDTO> itemsDto);
	
	Optional<VendingItemDTO> findVendingItemById(Long itemId);
	
	List<VendingItemDTO> findAllVendingItemById();
	
	VendingItemDTO updateVendingItem(VendingItemDTO itemDto);
	
	boolean changeActiveItem(Long itemId);
	
	boolean changeActiveItems(List<Long> itemIds);
	
	boolean removeVendingItem(Long itemId);
	
	boolean removeVendingItems(List<Long> itemIds);
	
	default VendingItemDTO toDTO(VendingItemEntity itemEntity) {
	
		VendingItemDTO itemDto = VendingItemDTO.builder()
								   			   .itemId(itemEntity.getItemId())
								   			   .adminId(itemEntity.getAdminId().getAdminId())
											   .name(itemEntity.getName())
											   .description(itemEntity.getDescription())
											   .itemType(itemEntity.getItemType())
											   .value(itemEntity.getValue())
											   .probability(itemEntity.getProbability())
											   .stock(itemEntity.getStock())
											   .totalUsed(defaultIfNull(itemEntity.getTotalUsed(), 0))
											   .totalClaim(defaultIfNull(itemEntity.getTotalClaim(), 0))
											   .active(defaultIfNull(itemEntity.getIsActive(), 1))
											   .regDate(itemEntity.getRegDate())
											   .upDate(itemEntity.getUpDate())
											   .build();
		return itemDto;
	}
	
	default VendingItemEntity toEntity(VendingItemDTO itemDto, Admin admin) {
		
		VendingItemEntity itemEntity = VendingItemEntity.builder()
														.itemId(itemDto.getItemId())
														.adminId(admin)
														.name(itemDto.getName())
														.description(itemDto.getDescription())
														.itemType(itemDto.getItemType())
														.value(itemDto.getValue())
														.probability(itemDto.getProbability())
														.stock(itemDto.getStock())
														.totalUsed(defaultIfNull(itemDto.getTotalUsed(), 0))
														.totalClaim(defaultIfNull(itemDto.getTotalClaim(), 0))
														.isActive(defaultIfNull(itemDto.getActive(), 1))
														.build();
		return itemEntity;
	}

}
