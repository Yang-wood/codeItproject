package com.codeit.mini.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.vending.IVendingMachinesRepository;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class VendingMachinesTests {
	
	@Autowired
	private IVendingMachinesRepository machinesRepository;

//	@Test
//	void testInsertVendingMachine() {
//		
//		IntStream.rangeClosed(1, 29).forEach(i -> {
//			VendingMachinesEntity vendingMachine = VendingMachinesEntity.builder()
//					.name("testVeningMachine")
//					.type("testVeningMachine")
//					.description("test")
//					.build();
//			
//			machinesRepository.save(vendingMachine);
//		});
//		
//	}
	
//	@Test
//	void testVendingMachineList() {
//		
//		List<VendingMachinesEntity> result = machinesRepository.findAll();
//		
//		result.forEach(vm -> {
//			log.info("자판기 아이디 : " + vm.getMachineId());
//			log.info("자판기 이름 : " + vm.getName());
//			log.info("자판기 종류 : " + vm.getType());
//			log.info("자판기 설명 : "+ vm.getDescription());
//		});
//		
//	}
	
//	@Test
//	void testVendingMachineRead() {
//		
//		Optional<VendingMachinesEntity> vm = machinesRepository.findById(1L);
//		
//		if (vm == null) {
//			log.info("해당 자판기가 존재하지 않습니다.");
//		}else {
//			
//			log.info("선택한 자판기는 : {} ", vm);
//		}
//		
//	}
	
//	@Test
//	void testVendingMachineUpdate() {
//		
//		Long vmId = 29L;
//		
//		VendingMachinesEntity vm = machinesRepository.findById(vmId).orElseThrow(() -> new RuntimeException("존재하지 않습니다."));
//		
//		vm.setName("testVm1");
//		vm.setType("testVmType1");
//		vm.setDescription("testVmDesc1");
//		
//		machinesRepository.save(vm);
//		
//		VendingMachinesEntity updateResult = machinesRepository.findById(vmId).orElseThrow(() -> new RuntimeException("존재하지 않습니다."));
//		
//		log.info("name: {}", updateResult.getName());
//		log.info("type: {}", updateResult.getType());
//		log.info("desc: {}",  updateResult.getDescription());
//		
//	}
	
//	@Test
//	void testVendingMachineRemove() {
//		
//		Long vmId = 29L;
//		
//		machinesRepository.deleteById(vmId);
//		
//		boolean result = machinesRepository.existsById(vmId);
//		
//		log.info("존재 여부 : {}", result);
//	}
}
