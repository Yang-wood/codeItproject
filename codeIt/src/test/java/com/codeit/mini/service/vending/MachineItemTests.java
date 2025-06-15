package com.codeit.mini.service.vending;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeit.mini.dto.vending.MachineItemDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.MachineItemId;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.IAdminRepository;
import com.codeit.mini.repository.vending.IMachineItemRepository;
import com.codeit.mini.repository.vending.IVendingItemRepository;
import com.codeit.mini.repository.vending.IVendingMachinesRepository;
import com.codeit.mini.service.vending.impl.MachineItemServiceImpl;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
@ExtendWith(MockitoExtension.class)
class MachineItemTests {
	
    @Autowired
    private IMachineItemService machineItemService;

    @Autowired
    private IVendingMachinesRepository vmRepository;

    @Autowired
    private IVendingItemRepository itemRepository;

    @Autowired
    private IMachineItemRepository machineItemRepository;
    
    @Autowired
    private IAdminRepository adminRepository;

//	@Test
//	void testregisterMachineItem() {
//		
//		Admin admin = adminRepository.getById(1L);
//		
//		VendingMachinesEntity vm = vmRepository.save(VendingMachinesEntity.builder()
//															              .name("테스트 자판기")
//															              .type("랜덤")
//															              .adminId(admin)
//															              .build()
//		);
//		
//		VendingItemEntity item = itemRepository.save(VendingItemEntity.builder()
//													                  .name("응시권")
//													                  .itemType("쿠폰")
//													                  .adminId(admin)
//													                  .isActive(1)
//													                  .build()
//		);
//		
//		MachineItemDTO dto = new MachineItemDTO(vm.getMachineId(), item.getItemId(), 0.3f);
//		
//		Long result = machineItemService.registerMachineItem(dto);
//		
//		assertEquals(vm.getMachineId(), result);
//		
//		assertTrue(machineItemRepository.existsById(new MachineItemId(vm.getMachineId(), item.getItemId())));
//	}
	
//	@Test
//	void testMachineItem() {
//		
//	    Long existingMachineId = 44L;
//	    Long existingItemId = 10L;
//
//	    MachineItemDTO dto = new MachineItemDTO(existingMachineId, existingItemId, 0.6f);
//
//	    Long result = machineItemService.registerMachineItem(dto);
//
//	    assertEquals(existingMachineId, result);
//	}

//	@Test
//	void testregisterMachineItems() {
//		
//		Admin admin = adminRepository.getById(1L);
//		
//        VendingMachinesEntity vm = vmRepository.save(VendingMachinesEntity.builder()
//																		  .name("자판기 A")
//																		  .type("랜덤")
//																		  .adminId(admin)
//																		  .isActive(1)
//																		  .build()
//		);
//
//        VendingItemEntity item1 = itemRepository.save(VendingItemEntity.builder()
//																	   .name("쿠폰1")
//																	   .itemType("쿠폰")
//																	   .adminId(admin)
//																	   .isActive(1)
//																	   .build()
//        );
//
//        VendingItemEntity item2 = itemRepository.save(VendingItemEntity.builder()
//                    												   .name("쿠폰2")
//                    												   .itemType("쿠폰")
//                    												   .adminId(admin)
//                    												   .isActive(1)
//                    												   .build()
//        );
//
//        List<MachineItemDTO> dtoList = List.of(
//        		new MachineItemDTO(vm.getMachineId(), item1.getItemId(), 0.3f),
//        		new MachineItemDTO(vm.getMachineId(), item2.getItemId(), 0.7f)
//        );
//
//        List<Long> resultIds = machineItemService.registerMachineItems(dtoList);
//
//        assertEquals(2, resultIds.size());
//        assertTrue(machineItemRepository.existsById(new MachineItemId(vm.getMachineId(), item1.getItemId())));
//        assertTrue(machineItemRepository.existsById(new MachineItemId(vm.getMachineId(), item2.getItemId())));
//        
//	}
	
//	@Test
//	void testMachineItems() {
//		Long machineId = 41L;
//		List<Long> itemIds = List.of(4L, 10L, 36L);
//
//		List<MachineItemDTO> dtoList = itemIds.stream()
//	            .map(itemId -> new MachineItemDTO(machineId, itemId, 1.0f / itemIds.size()))
//	            .collect(Collectors.toList());
//		
//		List<Long> result = machineItemService.registerMachineItems(dtoList);
//
//		assertEquals(3, result.size());
//        itemIds.forEach(itemId ->
//            			assertTrue(machineItemRepository.existsById(new MachineItemId(machineId, itemId)))
//        );
//	}
    
//	@Test
//	void testupdateMachineItem() {
//		Long machineId = 41L;
//        Long itemId = 10L;
//
//        MachineItemDTO dto = new MachineItemDTO(machineId, itemId, 0.9f);
//
//        MachineItemDTO updated = machineItemService.updateMachineItem(dto);
//
//        assertEquals(0.9f, updated.getProbability());
//	}
	
//	 @Test
//	 void testUpdateMachineItem_Fail_WhenNotConnected() {
//		 Long machineId = 999L;
//		 Long itemId = 888L;
//
//		 MachineItemDTO dto = new MachineItemDTO(machineId, itemId, 0.5f);
//
//		 assertThrows(IllegalArgumentException.class, () -> {
//			 machineItemService.updateMachineItem(dto);
//		 });
//	 }
    
//	@Test
//	void testfindAllItemsByMachineId() {
//		Long machineId = 41L;
//		
//		List<MachineItemDTO> result = machineItemService.findAllItemsByMachineId(machineId);
//		
//		log.info("조회된 아이템 수: {}", result.size());
//		
//		for (MachineItemDTO dto : result) {
//			log.info("1. 아이템 ID: {}, 2. 확률: {}, 3. 자판기 ID: {}", 
//			dto.getItemId(), dto.getProbability(), dto.getMachineId());
//	    }
//		
//		assertFalse(result.isEmpty());
//	}
	
//    @Test
//    void testfindMachineItemById() {
//    	
//		Long machineId = 41L;
//		Long itemId = 36L;
//		
//		Optional<MachineItemDTO> result = machineItemService.findMachineItemById(machineId, itemId);
//		
//		assertTrue(result.isPresent());
//		assertEquals(machineId, result.get().getMachineId());
//		assertEquals(itemId, result.get().getItemId());
//		
//		log.info("조회된 확률: {}", result.get().getProbability());
//        
//    }
    
//	@Test
//	void testchangeActiveMachine() {
//		Long machineId = 45L;
//		
//		VendingMachinesEntity before = vmRepository.findById(machineId)
//	            .orElseThrow(() -> new IllegalArgumentException("자판기 없음"));
//		
//		log.info("🔍 변경 전 isActive = {}", before.getIsActive());
//		
//		machineItemService.changeDeactiveMachine(machineId);
//		
//		VendingMachinesEntity after = vmRepository.findById(machineId)
//	            .orElseThrow(() -> new IllegalArgumentException("자판기 없음"));
//		
//		log.info("✅ 변경 후 isActive = {}", after.getIsActive());
//		
//		assertEquals(0, after.getIsActive());
//	}
    
//    @Test
//    void testReactivateMachine() {
//		Long machineId = 45L;
//		
//		VendingMachinesEntity before = vmRepository.findById(machineId)
//	            .orElseThrow(() -> new IllegalArgumentException("자판기 없음"));
//		
//		log.info("🔍 변경 전 isActive = {}", before.getIsActive());
//		
//		machineItemService.changeActivateMachine(machineId);
//		
//		VendingMachinesEntity after = vmRepository.findById(machineId)
//	            .orElseThrow(() -> new IllegalArgumentException("자판기 없음"));
//		
//		log.info("✅ 변경 후 isActive = {}", after.getIsActive());
//		
//		assertEquals(1, after.getIsActive());
//    }
    
//	@Test
//	void testremoveMachineItemId() {
//		Long machineId = 41L;
//		Long itemId = 4L;
//		
//		assertTrue(machineItemRepository.existsById(new MachineItemId(machineId, itemId)));
//		
//		boolean result = machineItemService.removeMachineItemId(machineId, itemId);
//
//        assertTrue(result);
//        assertFalse(machineItemRepository.existsById(new MachineItemId(machineId, itemId)));
//
//        log.info("자판기-아이템 연결 제거 완료");
//	}
    
//	@Test
//	void testremoveMachine() {
//		Long machineId = 46L;
//		
//		assertTrue(vmRepository.existsById(machineId));
//		
//		boolean result = machineItemService.removeMachine(machineId);
//		
//		assertTrue(result);
//		assertFalse(vmRepository.existsById(machineId));
//		
//		List<MachineItemEntity> remainingItems = machineItemRepository.findByVendingMachine_MachineId(machineId);
//	    assertTrue(remainingItems.isEmpty());
//		
//		log.info("자판기 및 연결된 아이템 제거 완료");
//	}
    
//	@Test
//	void testremoveAllByMachineId() {
//		Long machineId = 41L;
//
//        List<MachineItemEntity> beforeList = machineItemRepository.findByVendingMachine_MachineId(machineId);
//        assertFalse(beforeList.isEmpty(), "삭제 전 연결된 아이템이 존재해야 합니다.");
//
//        machineItemService.removeAllByMachineId(machineId);
//
//        List<MachineItemEntity> afterList = machineItemRepository.findByVendingMachine_MachineId(machineId);
//        assertTrue(afterList.isEmpty(), "삭제 후 연결된 아이템이 없어야 합니다.");
//
//        log.info("자판기 ID {}에 연결된 아이템 전체 삭제 성공", machineId);
//	}
	
}
