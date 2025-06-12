package com.codeit.mini.controller.vendingmachine;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendingmachines")
public class VendingController {
	
	/*
	 *  [회원 자판기 기능 구현]
	 * 1. 자판기 목록 조회			GET
	 * 2. 자판기 상세				GET
	 * 3. 자판기 뽑기 시작 / 결과		POST
	 * 4. 본인 자판기 이력 조회		GET
	 */
//	1. 자판기 목록 조회
	@GetMapping
	public void vendingMachinesList() {
		
	}
	
//	2. 자판기 상세
	@GetMapping("/{machineId}")
	public void vendingMachineRead() {
		
	}
	
//	3. 자판기 뽑기
	@PostMapping("/{machineId}/draw")
	public void vendingMachineDraw() {
		
	}
	
//	4. 회원 자판기 이력 조회
	@GetMapping("/{hitory}/{userId}")
	public void vendingUserHistory() {
		
	}
	
	
//	   // 자판기 목록
//    @GetMapping
//    public List<VendingMachineDTO> vendingMachines() {
//        // 자판기 목록 반환
//        return vendingMachineService.getAll();
//    }
//    
//    // 자판기 상세
//    @GetMapping("/{machineId}")
//    public VendingMachineDTO vedingMachineRead(@PathVariable Long machineId) {
//        return vendingMachineService.getById(machineId);
//    }
//    
//    // 자판기 뽑기
//    @PostMapping("/{machineId}/draw")
//    public DrawResultDTO vedingDraw(@PathVariable Long machineId) {
//        // 뽑기 결과 반환
//        return vendingMachineService.draw(machineId, ...);
//    }
    
}
