package com.codeit.mini.service.vending;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeit.mini.dto.vending.VendingItemDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.repository.IAdminRepository;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class VendingItemTests {
	
	@Autowired
	private IVendingItemService itemService;
	
	@Autowired
	private IAdminRepository adminRepository;
	
//	@Test
//	void InsertItem() {
//		
//		Optional<Admin> adiminId = adminRepository.findById(1L);
//		
//		Admin admin = adiminId.get();
//		
//		IntStream.rangeClosed(1, 30).forEach(i -> {
//			VendingItemDTO item = VendingItemDTO.builder()
//												.adminId(admin.getAdminId())
//												.name("TEST COUPON" + i)
//												.itemType(i + "COUPON")
//												.probability(0.4 + i + (i/10))
//												.build();
//			
//			log.info("생성된 아이템 " + i + "번째 정보 : " + item);
//			
//			itemService.registerItem(item);
//		});
//		
//	}
	
//	@Test
//	void readItem() {
//		Long targetId = 30L;
//		
//		Optional<VendingItemDTO> result = itemService.readItem(targetId);
//		
//		log.info("30번 아이템 상세 내용 : " + result);
//	}
	
//	@Test
//	void itemList() {
//		List<VendingItemDTO> itemList = itemService.itemList();
//		
//		itemList.forEach(item -> {
//			log.info("상품 번호 : {}", item.getItemId());
//			log.info("상품 생성자 : {}", item.getAdminId());
//			log.info("아이템 이름 : {}", item.getName());
//			log.info("상품 타입 : {}", item.getItemType());
//			log.info("상품 확률 : {}", item.getProbability());
//			log.info("상품 활성화 유무 : {}", item.getActive());
//			log.info("등록일 : {}", item.getRegDate());
//		});
//	
//	}
	
//	@Test
//	void itemModify() {
//		Long targetId = 30L;
//		
//		Optional<VendingItemDTO> itemOpt = itemService.readItem(targetId);
//		assertTrue(itemOpt.isPresent());
//		
//		VendingItemDTO origin = itemOpt.get();
//		
//		VendingItemDTO modifiy = VendingItemDTO.builder()
//											   .itemId(origin.getItemId())
//											   .name("상품명 수정 테스트")
//											   .description("상품 설명 수정 테스트")
//											   .itemType("쿠폰")
//											   .value(10)
//											   .probability(17.13)
//											   .stock(100)
//											   .active(1)
//											   .build();
//		
//		VendingItemDTO result = itemService.itemModify(modifiy);
//		
//		assertEquals("상품명 수정 테스트", result.getName());
//		
//		log.info("수정 전 상품 정보 : {}", origin);
//		log.info("수정된 상품 정보 : {}", result);
//		
//	}
	
//	@Test
//	void itemDelete() {
//		Long targetId = 33L;
//		
//		boolean result = itemService.removeItem(targetId);
//		
//		log.info("삭제 성공 여부 : {}", result);
//	}

}
