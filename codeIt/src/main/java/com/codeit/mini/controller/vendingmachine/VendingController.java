package com.codeit.mini.controller.vendingmachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.comm.UnifiedCouponHistoryDTO;
import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.dto.vending.MachineItemDTO;
import com.codeit.mini.dto.vending.TestCouponDTO;
import com.codeit.mini.dto.vending.VendingHistoryDTO;
import com.codeit.mini.dto.vending.VendingItemDTO;
import com.codeit.mini.dto.vending.VendingMachineDTO;
import com.codeit.mini.dto.vending.VendingResultDTO;
import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.VendingHistoryEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.vending.ICouponHistoryRepository;
import com.codeit.mini.repository.vending.IMachineItemRepository;
import com.codeit.mini.repository.vending.ITestCouponRepository;
import com.codeit.mini.service.vending.ICouponHistoryService;
import com.codeit.mini.service.vending.IMachineItemService;
import com.codeit.mini.service.vending.IPointHistoryService;
import com.codeit.mini.service.vending.ITestCouponService;
import com.codeit.mini.service.vending.IVendingHistoryService;
import com.codeit.mini.service.vending.IVendingMachineService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Log4j2
public class VendingController {
	
	private final IVendingMachineService vendingMachineService;
	private final IVendingHistoryService vendingHistoryService;
	private final IMachineItemService machinItemService;
	private final IMachineItemRepository machineItemRepository;
	private final ICouponHistoryService couponHistoryService;
	private final IPointHistoryService pointHistoryService;
	private final ICouponHistoryRepository couponRepository;
	private final ITestCouponRepository testCouponRepository;
	private final ITestCouponService testCouponService;
	
	/*
	 *  [회원 자판기 기능 구현]
	 * 1. 자판기 목록 조회			GET
	 * 2. 자판기 상세				GET
	 * 3. 자판기 뽑기 시작 / 결과		POST
	 * 4. 본인 자판기 이력 조회		GET
	 * 5. 쿠폰 발급 사용 이력 조회		GET
	 */
//	1. 자판기 목록 조회
	@GetMapping("/vendingmachine/active")
	public ResponseEntity<?> vendingMachinesList(@Validated PageRequestDTO requestDTO, HttpSession session) {
		
		log.info("자판기 목록 조회 컨트롤러");
		
		MemberDTO member = (MemberDTO) session.getAttribute("member");
	    
	    if (member == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	    }

	    Long memberId = member.getMemberId();

	    PageResultDTO<VendingMachineDTO, VendingMachinesEntity> result =
	            vendingMachineService.getVendingMachines(requestDTO);

	    return ResponseEntity.ok(result.getDtoList());
	}
	
//	2. 자판기 상세
	@GetMapping("/{machineId}")
	public ResponseEntity<?> vendingMachineRead(@PathVariable("machineId") Long machineId, HttpSession session) {
		MemberDTO member = (MemberDTO) session.getAttribute("member");

	    if (member == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	    }

	    Optional<VendingMachineDTO> result = vendingMachineService.findVendingMachineById(machineId);
	    
	    if (result.isEmpty() || result.get().getActive() != 1) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비활성화된 자판기입니다.");
	    }
	    
	    return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
//	2-1. 자판기 아이템 목록
	@GetMapping("/{machineId}/items")
	public ResponseEntity<?> machineItemListRead(@PathVariable("machineId") Long machineId, HttpSession session) {
	    log.info("🧲 자판기 아이템 조회: machineId={}", machineId);
	    MemberDTO member = (MemberDTO) session.getAttribute("member");
	    if (member == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	    }

	    List<MachineItemDTO> itemList = machinItemService.findAllItemsByMachineId(machineId);

	    log.info("📦 조회된 DTO 아이템 수: {}", itemList.size());
	    for (MachineItemDTO item : itemList) {
	        log.info("📦 itemId: {}, 이름: {}, 타입: {}", item.getItemId(), item.getName());
	    }

	    return ResponseEntity.ok(itemList);
	}
	
//	3. 자판기 뽑기
	@PostMapping("/{machineId}/draw")
	public ResponseEntity<?> vendingMachineDraw(@PathVariable("machineId") Long machineId, @RequestParam(required = false, name = "itemId") Long itemId,
            									HttpSession session) {
		MemberDTO member = (MemberDTO) session.getAttribute("member");

	    if (member == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	    }

	    try {
	        VendingResultDTO result = vendingMachineService.useVendingMachine(machineId, member.getMemberId(), itemId);
	        
	        List<TestCouponDTO> updatedCoupon = testCouponService.getCouponByMemberId(member.getMemberId())
	        										.stream()
	        										.filter(c -> c.getRemainCnt() != null && c.getRemainCnt() > 0)
	        										.collect(Collectors.toList());
	        session.setAttribute("testCoupons", updatedCoupon);
	        log.info("업데이트 쿠폰 정보 : {}", updatedCoupon);
	        
	        
	        return ResponseEntity.ok(result);
	    } catch (IllegalArgumentException e) {
	        // 비즈니스 로직 예외 (예: 포인트 부족, 아이템 없음 등)
	        log.warn("❌ 자판기 뽑기 실패 (사용자 에러): {}", e.getMessage());
	        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	    } catch (Exception e) {
	        // 기타 시스템 예외
	        log.error("🔥 자판기 뽑기 실패 (서버 내부 오류)", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류 발생");
	    }
	}
	
//	4. 회원 자판기 이력 조회
	@GetMapping("/{history}/{userId}")
	public ResponseEntity<?> vendingUserHistory(@ModelAttribute PageRequestDTO requestDTO,
            									HttpSession session) {

		MemberDTO member = (MemberDTO) session.getAttribute("member");

	    if (member == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	    }

	    PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> result =
	            vendingHistoryService.getHistoriesByMember(member.getMemberId(), requestDTO);

	    return ResponseEntity.ok(result);
	}
	
//	5. 회원 쿠폰 발급 이력 조회
	@GetMapping("/coupons/history")
	public ResponseEntity<?> getUserCouponHistory(@ModelAttribute PageRequestDTO requestDTO,
	                                              HttpSession session) {
		Long memberId = (Long) session.getAttribute("member");

		if (memberId == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	    }

	    PageResultDTO<UnifiedCouponHistoryDTO, UnifiedCouponHistoryDTO> result =
	    		couponHistoryService.getAllUserCouponsPaged(memberId, requestDTO);

	    return ResponseEntity.ok(result);
	}
	
	@GetMapping("/points")
	public ResponseEntity<Map<String, Integer>> getCurrentPoint(HttpSession session) {
		log.info("포인트를 조회합니다.");
        MemberDTO member = (MemberDTO) session.getAttribute("member");
        
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int point = pointHistoryService.getCurrentPoint(member.getMemberId());

        Map<String, Integer> result = new HashMap<>();
        result.put("points", point);
        return ResponseEntity.ok(result);
	}
	
	@PostMapping("/vendingmachines/{machineId}/purchase")
	public ResponseEntity<?> purchaseItems(@PathVariable("machineId") Long machineId,
	                                       @RequestBody Map<String, List<Long>> body,
	                                       HttpSession session) {
	    MemberDTO member = (MemberDTO) session.getAttribute("member");
	    if (member == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body(Map.of("error", "UNAUTHORIZED", "message", "로그인이 필요합니다."));
	    }

	    List<Long> itemIds = body.get("itemIds");
	    if (itemIds == null || itemIds.isEmpty()) {
	        return ResponseEntity.badRequest()
	                             .body(Map.of("error", "NO_ITEMS_SELECTED", "message", "아이템이 선택되지 않았습니다."));
	    }

	    try {
	        VendingResultDTO result = vendingMachineService.purchaseMultipleItems(machineId, member.getMemberId(), itemIds);
	        
	        List<TestCouponDTO> updatedCoupon = testCouponService.getCouponByMemberId(member.getMemberId())
	        										.stream()
	        										.filter(c -> c.getRemainCnt() != null && c.getRemainCnt() > 0)
	        										.collect(Collectors.toList());
	        session.setAttribute("testCoupons", updatedCoupon);
	        log.info("업데이트 쿠폰 정보 : {}", updatedCoupon);
	        
	        return ResponseEntity.ok(result);

	    } catch (IllegalStateException e) {
	        log.warn("❗비즈니스 로직 오류: {}", e.getMessage());
	        return ResponseEntity.badRequest()
	                             .body(Map.of("error", "포인트 부족!", "message", e.getMessage()));
	    } catch (Exception e) {
	        log.error("❌ 서버 오류 발생", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Map.of("error", "SERVER_ERROR", "message", "서버 내부 오류"));
	    }
	}
	
}
