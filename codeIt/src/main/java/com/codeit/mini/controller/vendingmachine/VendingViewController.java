package com.codeit.mini.controller.vendingmachine;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.dto.vending.VendingHistoryDTO;
import com.codeit.mini.entity.vending.VendingHistoryEntity;
import com.codeit.mini.service.vending.ICouponHistoryService;
import com.codeit.mini.service.vending.IVendingHistoryService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/vending")
@RequiredArgsConstructor
@Log4j2
public class VendingViewController {
	
	private final ICouponHistoryService couponHistoryService;
	private final IVendingHistoryService historyService;

    @GetMapping("/machines")
    public String vendingPage(Model model, HttpSession session) {
    	log.info("화면 진입");

        MemberDTO member = (MemberDTO) session.getAttribute("member");

        if (member == null) {
            return "redirect:/member/login";
        }

        model.addAttribute("memberName", member.getMemberName());
        model.addAttribute("memberId", member.getMemberId());

        return "vending/machines";
    }
    
    @GetMapping("/result")
    public String vendingResultPage() {
        return "vending/result";
    }
    // 마이페이지 내 뽑기 이력
    @GetMapping("/history")
    public String vendingHistoryPage(HttpSession session, Model model, PageRequestDTO requestDTO) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");
        if (member == null) return "redirect:/member/login";

        Long memberId = member.getMemberId();
        
        PageResultDTO<VendingHistoryDTO, VendingHistoryEntity> result =
        		historyService.getHistoriesByMember(memberId, requestDTO);
        
        model.addAttribute("result", result);
        
        return "vending/history";
    }
}
