package com.codeit.mini.controller.vendingmachine;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codeit.mini.dto.member.MemberDTO;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/vending")
@Log4j2
public class VendingViewController {

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
    public String vendingHistoryPage() {
        return "vending/history";
    }
}
