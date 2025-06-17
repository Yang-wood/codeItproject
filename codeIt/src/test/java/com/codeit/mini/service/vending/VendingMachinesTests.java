package com.codeit.mini.service.vending;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeit.mini.dto.vending.VendingMachineDTO;
import com.codeit.mini.entity.admin.Admin;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class VendingMachinesTests {
	
	@Autowired
	private IVendingMachineService machineService;
	
//	@Test
//	void registerVm() {
//		
//		IntStream.range(1, 5).forEach(i -> {
//			VendingMachineDTO machine = VendingMachineDTO.builder()
//														 .adminId(1L)
//														 .name("테스트 자판기" +i)
//														 .type("테스트 자판기 타입" +i)
//														 .build();
//			
//			log.info("생성된 자판기 " + i + "번째 정보 : " + machine);
//			
//			machineService.registerVendingMachine(machine);
//		});
//	}
	
//	@Test
//	void updateVmtest() {
//		Long vmId = 22L;
//		Long adminId = 1L;
//		
//		VendingMachineDTO vmDto = VendingMachineDTO.builder()
//												   .machineId(vmId)
//												   .adminId(adminId)
//												   .name("UPDATE VM")
//												   .type("UPDATE VM TYPE")
//												   .description("UDATE VM DESC")
//												   .active(0)
//												   .build();
//		
//		VendingMachineDTO updated = machineService.vmModify(vmDto);
//		
//		log.info("수정된 자판기 정보 : " + updated);
//		
//	}
	
//	@Test
//	void readVmTest() {
//		
//		Optional<VendingMachineDTO> result = machineService.readVm(31L);
//		assertTrue(result.isPresent());
//		assertEquals(31L, result.get().getMachineId());
//		
//		log.info("자판기 상세 정보 : " + result);
//		
//	}
	
//	@Test
//	void vmListTest() {
//		List<VendingMachineDTO> vmList = machineService.vmList(null);
//		
//		vmList.forEach(vm -> {
//			log.info("자판기 번호 {} ", vm.getMachineId());
//			log.info("자판기 생성자 {} ", vm.getAdminId() == null ? "null" : vm.getAdminId());
//			log.info("자판기 이름 {} ", vm.getName());
//			log.info("자판기 타입 {} ", vm.getType());
//			log.info("자판기 설명 {} ", vm.getDescription());
//			log.info("자판기 활성화 {} ", vm.getActive());
//			log.info("자판기 생성일 {} ", vm.getRegDate());
//		});
//	}

}
