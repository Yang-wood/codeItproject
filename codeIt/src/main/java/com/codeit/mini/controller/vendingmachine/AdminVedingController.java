package com.codeit.mini.controller.vendingmachine;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/vendingmachines")
public class AdminVedingController {
	
	/*
	 *  [관리자 자판기 기능 구현]
	 * 1. 자판기 목록					GET
	 * 2. 자판기 + 상품 등록				POST
	 * 3. 상품 등록					POST
	 * 4. 자판기 상세					GET
	 * 5. 등록된 상품 목록				GET
	 * 6. 자판기 + 상품 수정				PUT
	 * 7. 자판기 삭제					DELETE
	 * 8. 상품 삭제					DELETE
	 * 9. 자판기 이력 조회				GET
	 */
	
//	1. 자판기 목록
	@GetMapping
	public void vendingMachinesList() {
		
	}
	
//	2. 자판기 + 상품 등록
	@PostMapping
	public void VendingMachinesInsert() {
		
	}
	
//	3. 상품 등록
	@PostMapping ("/items")
	public void vendingItemInsert() {
		
	}
	
//	4. 자판기 상세
	@GetMapping("/{machineId}")
	public void getVendingMachineDetail() {
		
	}
	
//	5. 자판기 상품 목록
	@GetMapping ("/{machineId}/items")
	public void machinesItemRead() {
		
	}
	
//	6. 전체 상품 목록 (관리자 전체 상품 조회)
	@GetMapping("/items")
	public void vendingItemsList() {
		
	}
	
//	7. 자판기 + 상품 수정
	@PutMapping("/{machineId}")
	public void machinesUpdate() {
		
	}
	
//	8. 자판기 삭제
	@DeleteMapping("/{machineId}")
	public void machinesRemove() {
		
	}
	
//	9. 상품 삭제
	@DeleteMapping("/items/{itemId}")
	public void itemRemove() {
		
	}
	
//	10. 자판기 이력 조회
	@GetMapping("/history/{machineId}")
	public void vendingHistoryList() {
		
	}
	
}
