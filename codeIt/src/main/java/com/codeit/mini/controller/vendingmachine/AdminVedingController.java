package com.codeit.mini.controller.vendingmachine;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.codeit.mini.dto.AdminDTO;
import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.vending.CouponHistoryDTO;
import com.codeit.mini.dto.vending.CouponHistoryRequestDTO;
import com.codeit.mini.dto.vending.CouponStatusDTO;
import com.codeit.mini.dto.vending.MachineItemDTO;
import com.codeit.mini.dto.vending.RequestMachineItemDTO;
import com.codeit.mini.dto.vending.VendingHistoryDTO;
import com.codeit.mini.dto.vending.VendingItemDTO;
import com.codeit.mini.dto.vending.VendingMachineDTO;
import com.codeit.mini.dto.vending.VendingMachineUpdateRequestDTO;
import com.codeit.mini.dto.vending.VendingMachineWithItemsDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.vending.CouponHistoryEntity;
import com.codeit.mini.entity.vending.VendingHistoryEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.service.vending.ICouponHistoryService;
import com.codeit.mini.service.vending.IMachineItemService;
import com.codeit.mini.service.vending.IVendingHistoryService;
import com.codeit.mini.service.vending.IVendingItemService;
import com.codeit.mini.service.vending.IVendingMachineService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminVedingController {
	
	private final IVendingMachineService vendingMachineService;
	private final IVendingHistoryService vendingHistoryService;
	private final IVendingItemService itemService;
	private final IMachineItemService machineItemService;
	private final ICouponHistoryService couponHistoryService;
	
	/*
	 *  [관리자 자판기 기능 구현]
	 * 1. 자판기 목록						GET
	 * 2. 자판기 + 상품 등록					POST
	 * 3. 상품 등록						POST
	 * 4. 자판기 상세						GET
	 * 5. 등록된 상품 목록					GET
	 * 6. 자판기 + 상품 수정					PUT
	 * 7. 전체 상품 목록 (관리자 전체 상품 조회) 	GET
	 * 8. 자판기 삭제						DELETE
	 * 9. 상품 삭제						DELETE
	 * 10. 자판기 이력 조회					GET
	 * 11. 쿠폰 발급 이력 / 사용 조회			GET
	 */
	
//	1. 자판기 목록
	@GetMapping
	public ResponseEntity<?> vendingMachinesList(@ModelAttribute PageRequestDTO requestDTO, HttpSession session) {
	    PageResultDTO<VendingMachineDTO, VendingMachinesEntity> result = vendingMachineService.getVendingMachinesWithFilter(requestDTO);
	    return ResponseEntity.ok(result);
	}
	
//	2. 자판기 + 상품 등록
	@PostMapping
	public ResponseEntity<?> insertVendingMachineWithItems(@RequestBody VendingMachineWithItemsDTO request, HttpSession session) {
		 try {
		        VendingMachineDTO vmDto = request.getVendingMachine();
		        List<MachineItemDTO> machineItems = request.getItemIds();

		        AdminDTO admin = (AdminDTO) session.getAttribute("admin");
		        if (admin == null) {
		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		        }
		        vmDto.setAdminId(admin.getAdminId());
		        
		        if ("RANDOM".equalsIgnoreCase(vmDto.getType())) {
		            double totalProbability = machineItems.stream()
		                    .mapToDouble(MachineItemDTO::getProbability)
		                    .sum();

		            if (totalProbability > 100.0) {
		                return ResponseEntity.badRequest().body("⚠ 확률 총합이 100%를 초과합니다.");
		            }
		        }

		        Long vmId = vendingMachineService.registerVendingMachine(vmDto);

		        if (machineItems != null && !machineItems.isEmpty()) {
		            for (MachineItemDTO miDto : machineItems) {
		                miDto.setMachineId(vmId);
		                machineItemService.registerMachineItem(miDto);
		            }
		        }

		        return ResponseEntity.ok("자판기 및 상품 등록 완료! 자판기 ID: " + vmId);

		    } catch (IllegalArgumentException e) {
		        return ResponseEntity.badRequest().body(e.getMessage());

		    } catch (Exception e) {
		        log.error("[자판기 등록 오류]", e);
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
		    }
		}
	
//	3. 상품 등록
	@PostMapping ("/items")
    public ResponseEntity<?> registerVendingItem(@RequestBody VendingItemDTO dto, HttpSession session){
        AdminDTO admin = (AdminDTO) session.getAttribute("admin");
        
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        dto.setAdminId(admin.getAdminId());
        
		try {
            Long itemId = itemService.registerVendingItem(dto);
            return ResponseEntity.ok("상품 등록 완료! 상품 ID: " + itemId);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
            
        } catch (Exception e) {
            log.error("[상품 등록 오류]", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
	}
	
//	4. 자판기 상세
	@GetMapping("/{machineId}")
    public ResponseEntity<?> getVendingMachineDetail(@PathVariable("machineId") Long machineId) {
		Optional<VendingMachineDTO> result = vendingMachineService.findVendingMachineById(machineId);

		if (result.isPresent()) {
			return ResponseEntity.ok(result.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("자판기 정보를 찾을 수 없습니다.");
		}
	}
	
//	5. 자판기 상품 목록
	@GetMapping ("/{machineId}/items")
    public ResponseEntity<?> machinesItemRead(@PathVariable("machineId") Long machineId) {
		return ResponseEntity.ok(machineItemService.findAllItemsByMachineId(machineId));
    }
	
//	6. 전체 상품 목록 (관리자 전체 상품 조회)
	@GetMapping("/items")
    public ResponseEntity<?> vendingItemsList() {
		List<VendingItemDTO> dtoList = itemService.findAllVendingItemById();
		return ResponseEntity.ok(dtoList);
    }
	
//	7. 자판기 + 상품 수정
	@PutMapping("/{machineId}")
    public ResponseEntity<?> machinesUpdate(@PathVariable("machineId") Long machineId, 
    										@RequestBody VendingMachineUpdateRequestDTO request) {
		log.info("받은 전체 요청: {}", request);
		log.info("자판기 정보: {}", request.getVendingMachine());
		log.info("아이템 리스트: {}", request.getItemIds());
		try {
	        request.getVendingMachine().setMachineId(machineId);
	        vendingMachineService.updateVendingMachineWithItems(request);
	        return ResponseEntity.ok("자판기 및 연결 아이템 수정 완료");
	        
	    } catch (IllegalArgumentException e) {
	        log.error("🚨 자판기 업데이트 중 오류 발생", e);
	        e.printStackTrace();
	        return ResponseEntity.badRequest().body(e.getMessage());
	        
	    } catch (Exception e) {
	        log.error("자판기 수정 중 오류", e);
	        e.printStackTrace();
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
	    }
	}
		
		//	    try {
//	        VendingMachineDTO vmDto = request.getVendingMachine();
//	        vmDto.setMachineId(machineId);
//	        vendingMachineService.updateVendingMachine(vmDto);
//
//	        machineItemService.removeAllByMachineId(machineId);
//
//	        List<RequestMachineItemDTO> items = request.getItemIds();
//
//	        log.info("▶️ 수정 요청 받은 자판기 정보: {}", machineId);
//	        log.info("▶️ 상품 목록: {}", items);
//
//	        List<MachineItemDTO> machineItemList = items.stream().map(reqItem -> {
//	            MachineItemDTO dto = new MachineItemDTO();
//	            dto.setItemId(reqItem.getItemId());
//	            dto.setProbability(reqItem.getProbability());
//	            dto.setMachineId(machineId);
//	            return dto;
//	        }).collect(Collectors.toList());
//
//	        for (MachineItemDTO item : machineItemList) {
//	            machineItemService.registerMachineItem(item);
//	        }
//
//	        return ResponseEntity.ok("자판기 및 상품 수정 완료");
//	    } catch (IllegalArgumentException e) {
//	        return ResponseEntity.badRequest().body(e.getMessage());
//	    } catch (Exception e) {
//	        log.error("[자판기 수정 오류]", e);
//	        e.printStackTrace(); // 추가
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
//	    }

	
//	8. 자판기 삭제
	@DeleteMapping("/{machineId}")
	public ResponseEntity<?> machinesRemove(@PathVariable("machineId") Long machineId) {
		boolean result = vendingMachineService.removeVendingMachine(machineId);
		return result ? ResponseEntity.ok("삭제 성공") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("자판기를 찾을 수 없음");
	}
	
//	9. 상품 삭제
	@DeleteMapping("/items/{itemId}")
	public ResponseEntity<?> itemRemove(@PathVariable("itemId") Long itemId) {
		boolean result = itemService.removeVendingItem(itemId);
		return result ? ResponseEntity.ok("상품 삭제 완료") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품 없음");
    }
	
//	10. 자판기 이력 조회
    @GetMapping("/history/{machineId}")
    public ResponseEntity<?> vendingHistoryList(@PathVariable("machineId") Long machineId,@RequestParam(required = false) Long itemId,
            									@ModelAttribute PageRequestDTO requestDTO) {
    	try {
            PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> result;
            
            if (itemId != null) {
                result = vendingHistoryService.getHistoriesByMachine(machineId, itemId, requestDTO);
            } else {
                result = vendingHistoryService.searchHistories(null, machineId, null, null, requestDTO);
            }
            
            return ResponseEntity.ok(result);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
            
        } catch (Exception e) {
            log.error("[자판기 이력 조회 오류]", e);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }
    
//	11. 자판기 쿠폰 발급 상세 이력
    @GetMapping("/dashboard/history/{itemId}")
    public ResponseEntity<?> getCouponHistoryByItem(@PathVariable("itemId") Long itemId,
    												@ModelAttribute PageRequestDTO requestDTO) {
    	
    	Pageable pageable = PageRequest.of(requestDTO.getPage() - 1, requestDTO.getSize(), Sort.by("issuedDate").descending());
    	
    	Page<CouponHistoryEntity> historyPage = couponHistoryService.getCouponHistoryByItem(itemId, pageable);

    	Function<CouponHistoryEntity, CouponHistoryDTO> fn = coupon -> CouponHistoryDTO.builder()
																			           .couponCode(coupon.getCouponCode())
																			           .loginId(coupon.getMemberId().getLoginId())
																			           .issuedDate(coupon.getIssuedDate())
																			           .status(coupon.getStatus().name())
																			           .expireDate(coupon.getExpireDate())
																			           .build();

        return ResponseEntity.ok(new PageResultDTO<>(historyPage, fn));
    }
    
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkNameExists(@RequestParam("name") String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.ok(false);
        }

        boolean exists = vendingMachineService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
    
    @PutMapping("/items/{itemId}")
    public ResponseEntity<?> updateVendingItem(@PathVariable("itemId") Long itemId,
                                               @RequestBody VendingItemDTO dto,
                                               @SessionAttribute(name = "admin", required = false) AdminDTO admin) {
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        dto.setItemId(itemId);
        dto.setAdminId(admin.getAdminId());

        try {
            itemService.updateVendingItem(dto); // itemId 기준으로 내부에서 수정 처리
            return ResponseEntity.ok("상품 수정 완료");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            log.error("[상품 수정 오류]", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }
}
