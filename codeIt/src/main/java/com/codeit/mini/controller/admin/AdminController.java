package com.codeit.mini.controller.admin;

import java.util.Optional;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeit.mini.dto.AdminDTO;
import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.service.admin.IAdminService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {

private final IAdminService adminService;
	
	// 관리자 로그인 폼
    @GetMapping("/login")
    public String loginForm() {
        return "admin/login";
    }
    
    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam("adminLoginId") String adminLoginId,
    					@RequestParam("adminPw") String adminPw,
                        HttpSession session,
                        Model model) {

        Optional<AdminDTO> result = adminService.login(adminLoginId, adminPw);
        if (result.isPresent()) {
            session.setAttribute("admin", result.get());
            return "redirect:/codeit";
        } else {
            model.addAttribute("msg", "로그인 실패");
            return "admin/login";
        }
    }
    
    // 관리자 대시보드
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard"; // 자유 구성
    }
    
    // 회원 목록 조회 + 페이징 + 검색
    @GetMapping("/members")
    public String memberList(@ModelAttribute PageRequestDTO pageRequestDTO, Model model) {
        PageResultDTO<MemberDTO, ?> result = adminService.getMemberList(pageRequestDTO);
        model.addAttribute("result", result);
        return "admin/members";
    }
    
    // 회원 수정 폼
    @GetMapping("/members/edit")
    public String editMemberForm(@RequestParam("memberId") Long memberId, Model model) {
        Optional<MemberDTO> result = adminService.read(memberId);
        if (result.isPresent()) {
            model.addAttribute("dto", result.get());
            return "admin/member_edit";
        } else {
            model.addAttribute("msg", "해당 회원이 존재하지 않습니다.");
            return "redirect:/admin/members";
        }
    }
    
    // 회원 수정 처리
    @PostMapping("/members/edit")
    public String editMember(MemberDTO dto, RedirectAttributes redirectAttributes) {
        adminService.updateMember(dto);
        redirectAttributes.addAttribute("memberId", dto.getMemberId());
        return "redirect:/admin/members/edit";
    }
    
    // 회원 삭제 처리
    @PostMapping("/members/delete")
    public String deleteMember(@RequestParam Long memberId) {
        adminService.removeMember(memberId);
        return "redirect:/admin/members";
    }
    
    @GetMapping("/members/detail")
    public String memberDetail(@RequestParam("memberId") Long memberId, Model model) {
        Optional<MemberDTO> result = adminService.read(memberId);

        if (result.isPresent()) {
            model.addAttribute("dto", result.get());
            return "admin/member_detail";
        } else {
            model.addAttribute("msg", "해당 회원 없음");
            return "redirect:/admin/members";
        }
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
