package com.codeit.mini.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.vending.TestCouponDTO;
import com.codeit.mini.dto.vending.VendingHistoryDTO;
import com.codeit.mini.dto.vending.VendingResultDTO;
import com.codeit.mini.entity.comm.CouponStatusEnum;
import com.codeit.mini.entity.comm.VendingType;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.CouponHistoryEntity;
import com.codeit.mini.entity.vending.MachineItemEntity;
import com.codeit.mini.entity.vending.MachineItemId;
import com.codeit.mini.entity.vending.PointHistoryEntity;
import com.codeit.mini.entity.vending.TestCouponEntity;
import com.codeit.mini.entity.vending.VendingHistoryEntity;
import com.codeit.mini.entity.vending.VendingItemEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.vending.ICouponHistoryRepository;
import com.codeit.mini.repository.vending.IMachineItemRepository;
import com.codeit.mini.repository.vending.ITestCouponRepository;
import com.codeit.mini.repository.vending.IVendingItemRepository;
import com.codeit.mini.repository.vending.IVendingMachinesRepository;
import com.codeit.mini.service.vending.ICouponHistoryService;
import com.codeit.mini.service.vending.IPointHistoryService;
import com.codeit.mini.service.vending.ITestCouponService;
import com.codeit.mini.service.vending.IVendingHistoryService;
import com.codeit.mini.service.vending.IVendingMachineService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class VendingMachineUseTest {
	
    @Autowired
    private IVendingMachineService vendingMachineService;
    @Autowired
    private IMemberRepository memberRepository;
    @Autowired
    private IVendingMachinesRepository vendingRepository;
    @Autowired
    private IVendingItemRepository itemRepository;
    @Autowired
    private IMachineItemRepository machineItemRepository;
    @Autowired
    private IPointHistoryService pointHistoryService;
    @Autowired
    private ICouponHistoryService couponService;
    @Autowired
    private ICouponHistoryRepository couponHistoryRepository;
    @Autowired
    private IVendingHistoryService vendingHistoryService;
    @Autowired
    private ITestCouponService testCouponService;
    @Autowired
    private ITestCouponRepository couponRepository;
    @PersistenceContext
    private EntityManager em;
    
//    @Test
//    void issueTestCoupon_shouldLogRemainCntProperly() {
//    	
//    	MemberEntity member = memberRepository.save(MemberEntity.builder()
//        .loginId("testUser")
//        .memberPw("pw")
//        .memberName("테스터")
//        .memberEmail("test@example.com")
//        .points(3000)
//        .status(0)
//        .role(0)
//        .emailVerified('Y')
//        .termsAgreed('Y')
//        .build());
//        VendingItemEntity item = itemRepository.save(VendingItemEntity.builder().name("무료 뽑기권").build());
//        VendingMachinesEntity vm = vendingRepository.save(VendingMachinesEntity.builder().type("RANDOM").name("랜덤자판기").build());
//        machineItemRepository.save(newMachineItem(vm, item, 1.0));
//
//        int totalCnt = 3;
//
//        // 2. 메서드 호출
//        TestCouponDTO dto = testCouponService.issueTestCoupon(
//            member.getMemberId(),
//            item.getItemId(),
//            totalCnt,
//            vm.getMachineId()
//        );
//
//        // 3. 검증
//        Assertions.assertNotNull(dto);
//        Assertions.assertEquals(totalCnt, dto.getRemainCnt());
//
//        log.info("✔ 최종 확인 - 발급된 remainCnt: {}", dto.getRemainCnt());
//    }
    
//    @Test
//    void fullRandomVendingMachineFlowTest() {
//        // 1. 회원 생성
//        MemberEntity member = memberRepository.save(MemberEntity.builder()
//            .loginId("testUser")
//            .memberPw("pw")
//            .memberName("테스터")
//            .memberEmail("test@example.com")
//            .points(3000)
//            .status(0)
//            .role(0)
//            .emailVerified('Y')
//            .termsAgreed('Y')
//            .build());
//
//        // 2. 자판기 생성
//        VendingMachinesEntity vm = vendingRepository.save(
//            VendingMachinesEntity.builder()
//                .name("확률자판기")
//                .type("RANDOM")
//                .isActive(1)
//                .build()
//        );
//
//        // 3. 아이템 생성
//        VendingItemEntity fail = itemRepository.save(newItem("꽝", "fail", 0));
//        VendingItemEntity rent = itemRepository.save(newItem("대여권", "rent", 0));
//        VendingItemEntity point = itemRepository.save(newItem("포인트100", "point", 100));
//        VendingItemEntity free = itemRepository.save(newItem("무료 뽑기권", "free", 0));
//
//        em.flush(); em.clear();
//
//        VendingMachinesEntity savedVm = vendingRepository.findById(vm.getMachineId()).get();
//        VendingItemEntity pFail = itemRepository.findById(fail.getItemId()).get();
//        VendingItemEntity pRent = itemRepository.findById(rent.getItemId()).get();
//        VendingItemEntity pPoint = itemRepository.findById(point.getItemId()).get();
//        VendingItemEntity pFree = itemRepository.findById(free.getItemId()).get();
//
//        machineItemRepository.saveAll(List.of(
//            newMachineItem(savedVm, pFail, 4.0),
//            newMachineItem(savedVm, pRent, 2.0),
//            newMachineItem(savedVm, pPoint, 3.0),
//            newMachineItem(savedVm, pFree, 1.0)
//        ));
//
//        em.flush(); em.clear();
//
//        // 6. 검증
//        assertTrue(machineItemRepository.findMachineItem(savedVm.getMachineId(), pFree.getItemId()).isPresent(), "자판기-아이템 연결 실패");
//
//        // 7. 쿠폰 선 발급 (무료 뽑기권)
//        testCouponService.issueTestCoupon(member.getMemberId(), pFree.getItemId(), 1, savedVm.getMachineId());
//
//        // 8. 자판기 사용 10회
//        int totalUsedPoint = 0;
//        int totalRewardPoint = 0;
//        int freeUsed = 0;
//
//        for (int i = 1; i <= 10; i++) {
//            VendingResultDTO result = vendingMachineService.useVendingMachine(savedVm.getMachineId(), member.getMemberId(), null);
//
//            log.info("🎯 [{}회차] 아이템명: {}, 차감P: {}, 보상P: {}, 쿠폰코드: {}",
//                i, result.getItemName(), result.getCostPoint(), result.getRewardPoint(), result.getCouponCode());
//
//            if (result.getCostPoint() == 0) {
//                freeUsed++;
//            }
//
//            totalUsedPoint += result.getCostPoint();
//            totalRewardPoint += result.getRewardPoint();
//        }
//
//        // 9. 통계 확인
//        log.info("========== 통계 요약 ==========");
//        log.info("총 사용 포인트: {}", totalUsedPoint);
//        log.info("총 보상 포인트: {}", totalRewardPoint);
//        log.info("무료 뽑기권 사용 횟수: {}", freeUsed);
//        log.info("최종 잔여 포인트: {}", memberRepository.findById(member.getMemberId()).get().getPoints());
//        log.info("================================");
//
//        assertEquals(1, freeUsed, "무료 뽑기권은 정확히 1번 사용되어야 함");
//    }
    
//    @Test
//    void rentalCouponIssuedMultipleTimes() {
//        MemberEntity member = memberRepository.save(MemberEntity.builder()
//            .loginId("user5").memberPw("pw").memberEmail("u@test.com")
//            .memberName("테스터").points(0).status(0).role(0).emailVerified('Y').termsAgreed('Y')
//            .build());
//
//        VendingMachinesEntity machine = vendingRepository.save(VendingMachinesEntity.builder()
//            .name("랜덤자판기").type(VendingType.RANDOM).isActive(1).build());
//
//        VendingItemEntity rentItem = itemRepository.save(newItem("대여권", "rental", 0));
//
//        machineItemRepository.save(newMachineItem(machine, rentItem, 100.0));
//
//        CouponHistoryEntity first = couponService.issueCoupon(member.getMemberId(), rentItem.getItemId(), machine.getMachineId());
//        CouponHistoryEntity second = couponService.issueCoupon(member.getMemberId(), rentItem.getItemId(), machine.getMachineId());
//
//        assertNotEquals(first.getCouponCode(), second.getCouponCode(), "쿠폰코드는 중복되면 안됨");
//        assertNotNull(first.getExpireDate(), "랜덤 자판기 쿠폰은 만료일이 있어야 함");
//        assertEquals(CouponStatusEnum.ISSUED, first.getStatus());
//        assertEquals(CouponStatusEnum.ISSUED, second.getStatus());
//
//        List<CouponHistoryEntity> coupons = couponHistoryRepository.findAllByMemberId(member);
//        assertEquals(2, coupons.size(), "랜덤 자판기에서는 동일 아이템이라도 쿠폰이 중복 발급되어야 함");
//    }
    

//    @Test
//    void noDuplicateInNormalMachine() {
//        MemberEntity member = memberRepository.save(newMember("user4"));
//
//        VendingMachinesEntity machine = vendingRepository.save(newMachine("일반자판기", VendingType.CHOICE));
//        VendingItemEntity item = itemRepository.save(newItem("할인권", "discount", 0));
//
//        machineItemRepository.save(newMachineItem(machine, item, 100.0));
//
//        couponService.issueCoupon(member.getMemberId(), item.getItemId(), machine.getMachineId());
//
//        assertThrows(IllegalStateException.class, () -> {
//        	couponService.issueCoupon(member.getMemberId(), item.getItemId(), machine.getMachineId());
//        }, "일반 자판기에서는 동일 쿠폰 중복 발급이 차단되어야 함");
//    }
    
//    private VendingItemEntity newItem(String name, String type, int value) {
//        return VendingItemEntity.builder()
//                .name(name)
//                .itemType(type)
//                .value(value)
//                .build();
//    }
//
//    private MemberEntity newMember(String loginId) {
//        return MemberEntity.builder()
//                .loginId(loginId)
//                .memberPw("pw")
//                .memberEmail(loginId + "@test.com")
//                .memberName("테스터")
//                .points(1000)
//                .status(0)
//                .role(0)
//                .emailVerified('Y')
//                .termsAgreed('Y')
//                .build();
//    }
//
//    private VendingMachinesEntity newMachine(String name, VendingType type) {
//        return VendingMachinesEntity.builder()
//                .name(name)
//                .type(type)
//                .isActive(1)
//                .build();
//    }
//
//    private MachineItemEntity newMachineItem(VendingMachinesEntity vm, VendingItemEntity item, double rate) {
//        return MachineItemEntity.builder()
//        		.id(new MachineItemId(vm.getMachineId(), item.getItemId()))
//                .vendingMachine(vm)
//                .vendingItem(item)
//                .probability(rate)
//                .build();
//    }
	
//	@Test
//	void vendingHistory() {
//		MemberEntity member = memberRepository.findById(42L)
//			        .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
//		
//		VendingMachinesEntity vm = vendingRepository.save(
//	    VendingMachinesEntity.builder().name("선택형자판기").type(VendingType.CHOICE).isActive(1).build()
//	);
//	
//	// 3. 쿠폰 아이템 생성 (예: 할인권)
//	VendingItemEntity item = itemRepository.save(
//	    VendingItemEntity.builder()
//	        .name("할인권")
//	        .itemType("discount")
//	        .value(0)
//	        .stock(1)
//	        .isActive(1)
//	        .build()
//	);
//	
//	// 4. 자판기-아이템 연결
//	machineItemRepository.save(MachineItemEntity.builder()
//	    .id(new MachineItemId(vm.getMachineId(), item.getItemId()))
//	    .vendingItem(item)
//	    .vendingMachine(vm)
//	    .probability(1.0)
//	    .build());
//	
//	// 5. 자판기 사용 (SELECT 타입은 원하는 아이템을 직접 지정)
//	vendingMachineService.useVendingMachine(vm.getMachineId(), member.getMemberId(), item.getItemId());
//	
//	// 6. 사용 이력 조회
//	PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> result =
//	    vendingHistoryService.getHistoriesByMember(member.getMemberId(), new PageRequestDTO());
//	
//	// 7. 결과 검증
//		assertThat(result.getDtoList()).isNotEmpty();
//		result.getDtoList().forEach(h -> {
//		    System.out.println("📜 사용 이력: 아이템 = " + h.getItemName() + ", 쿠폰코드 = " + h.getCouponCode());
//		    assertThat(h.getCouponCode()).isNotNull(); // 쿠폰이 포함된 이력이어야 함
//		});
//	
//	}
	
//    @Test
//    void 랜덤_아이템타입_쿠폰_발급_테스트() {
//        MemberEntity member = memberRepository.save(MemberEntity.builder()
//                .loginId("random_user")
//                .memberPw("pw")
//                .memberEmail("rand@test.com")
//                .memberName("랜덤유저")
//                .points(0)
//                .status(0)
//                .role(0)
//                .emailVerified('Y')
//                .termsAgreed('Y')
//                .build());
//
//        // 무작위 자판기 생성
//        VendingMachinesEntity machine = vendingRepository.save(VendingMachinesEntity.builder()
//                .name("랜덤자판기_" + UUID.randomUUID())
//                .type(VendingType.RANDOM)
//                .isActive(1)
//                .build());
//
//        // 아이템 타입 배열
//        String[] itemTypes = {"rental", "discount", "exam", "free"};
//        Random random = new Random();
//
//        for (int i = 0; i < 5; i++) {
//            String randomType = itemTypes[random.nextInt(itemTypes.length)];
//
//            VendingItemEntity item = itemRepository.save(VendingItemEntity.builder()
//                    .name(randomType + "_item_" + i)
//                    .itemType(randomType)
//                    .value(0)
//                    .stock(10)
//                    .isActive(1)
//                    .build());
//
//            machineItemRepository.save(MachineItemEntity.builder()
//                    .id(new MachineItemId(machine.getMachineId(), item.getItemId()))
//                    .vendingItem(item)
//                    .vendingMachine(machine)
//                    .probability(1.0)
//                    .build());
//
//            // 쿠폰 발급 (free는 스킵 가능)
//            if (!randomType.equals("free")) {
//                CouponHistoryEntity coupon = couponService.issueCoupon(member.getMemberId(), item.getItemId(), machine.getMachineId());
//                System.out.println("✅ 발급된 쿠폰: " + coupon.getCouponCode() + " (" + coupon.getCouponType() + ")");
//            }
//        }
//    }
    
//    @Test
//    @Transactional
//    void 발급된_쿠폰_검색_테스트() {
//
//        List<CouponHistoryEntity> issuedCoupons = couponHistoryRepository.findAllByStatus(CouponStatusEnum.ISSUED);
//
//        issuedCoupons.forEach(c -> {
//            System.out.println("🎫 쿠폰코드: " + c.getCouponCode() + " / 타입: " + c.getCouponType());
//        });
//
//        assertThat(issuedCoupons).isNotEmpty();
//    }
    
//    @Test
//    @Transactional
//    void 무료_뽑기권_3회_발급_후_차감_테스트() {
//        // 1. 회원 생성
//        MemberEntity member = memberRepository.save(MemberEntity.builder()
//                .loginId("freeuser")
//                .memberPw("pw")
//                .memberEmail("free@test.com")
//                .memberName("무료테스터")
//                .points(0)
//                .status(0)
//                .role(0)
//                .emailVerified('Y')
//                .termsAgreed('Y')
//                .build());
//
//        // 2. 자판기 및 무료 쿠폰 아이템 생성
//        VendingMachinesEntity machine = vendingRepository.save(VendingMachinesEntity.builder()
//                .name("무료자판기")
//                .type(VendingType.RANDOM)
//                .isActive(1)
//                .build());
//
//        VendingItemEntity freeItem = itemRepository.save(VendingItemEntity.builder()
//                .name("무료 뽑기권")
//                .itemType("free")
//                .value(0)
//                .stock(10)
//                .isActive(1)
//                .build());
//
//        machineItemRepository.save(MachineItemEntity.builder()
//                .id(new MachineItemId(machine.getMachineId(), freeItem.getItemId()))
//                .vendingItem(freeItem)
//                .vendingMachine(machine)
//                .probability(1.0)
//                .build());
//
//        // 3. 무료 뽑기권 쿠폰 발급
//        TestCouponDTO coupon = testCouponService.issueTestCoupon(member.getMemberId(), freeItem.getItemId(), 3, machine.getMachineId());
//
//        assertThat(coupon.getRemainCnt()).isEqualTo(3);
//        System.out.println("✅ 발급된 무료 뽑기권 코드: " + coupon.getCouponCode());
//
//        for (int i = 1; i <= 2; i++) {
//            TestCouponDTO dto = testCouponService.useTestCoupon(member.getMemberId(), freeItem.getItemId());
//
//            System.out.println("🔁 " + i + "회 사용 - 남은 횟수: " + dto.getRemainCnt());
//
//            if (i < 3) {
//                assertThat(dto.getRemainCnt()).isEqualTo(3 - i);
//                assertThat(dto.getStatus()).isEqualTo(CouponStatusEnum.ISSUED);
//            } else {
//                assertThat(dto.getRemainCnt()).isEqualTo(0);
//                assertThat(dto.getStatus()).isEqualTo(CouponStatusEnum.USED);
//            }
//        }
//
//    }
    
//    @Test
//    @Transactional
//    void 만료된_쿠폰_테스트() {
//        // 1. 회원 생성
//        MemberEntity member = memberRepository.save(MemberEntity.builder()
//            .loginId("expiredUser")
//            .memberPw("pw")
//            .memberEmail("expire@test.com")
//            .memberName("만료테스트")
//            .points(0)
//            .status(0)
//            .role(0)
//            .emailVerified('Y')
//            .termsAgreed('Y')
//            .build());
//
//        // 2. 쿠폰 아이템 생성
//        VendingItemEntity item = itemRepository.save(VendingItemEntity.builder()
//            .name("만료용 아이템")
//            .itemType("exam")
//            .value(0)
//            .stock(10)
//            .isActive(1)
//            .build());
//
//        VendingMachinesEntity machine = vendingRepository.save(VendingMachinesEntity.builder()
//            .name("만료자판기")
//            .type(VendingType.RANDOM)
//            .isActive(1)
//            .build());
//
//        machineItemRepository.save(MachineItemEntity.builder()
//            .id(new MachineItemId(machine.getMachineId(), item.getItemId()))
//            .vendingItem(item)
//            .vendingMachine(machine)
//            .probability(1.0)
//            .build());
//
//        // 3. 쿠폰 발급
//        CouponHistoryEntity coupon = couponService.issueCoupon(member.getMemberId(), item.getItemId(), machine.getMachineId());
//
//        // 4. 만료일 과거로 강제 설정
//        coupon.setExpireDate(LocalDateTime.now().minusDays(1));
//        coupon.setStatus(CouponStatusEnum.EXPIRED);
//        couponHistoryRepository.save(coupon);
//
//        // 5. 조회 시 만료 여부 확인
//        CouponHistoryEntity found = couponHistoryRepository.findById(coupon.getCouponId())
//            .orElseThrow();
//
//        assertThat(found.getStatus()).isEqualTo(CouponStatusEnum.EXPIRED);
//        assertThat(found.getExpireDate()).isBefore(LocalDateTime.now());
//        System.out.println("🔒 만료 쿠폰 확인됨: " + found.getCouponCode());
//    }

}
