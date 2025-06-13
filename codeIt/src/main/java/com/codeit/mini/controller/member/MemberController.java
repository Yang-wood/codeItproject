package com.codeit.mini.controller.member;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.service.member.IMemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Log4j2
public class MemberController {

	private final IMemberService memberService;
	
	// 회원 가입 페이지 이동
    @GetMapping("/register")
    public String registerForm() {
        return "/member/register";
    }
    
    // 회원 가입 처리
    @PostMapping("/register")
    public String register(MemberDTO dto, RedirectAttributes rttr) {
        log.info("회원 가입 요청 : " + dto);
        Long memberId = memberService.register(dto);
        rttr.addFlashAttribute("msg", "회원 가입이 완료되었습니다. (ID : " + memberId + ")");
        return "redirect:/member/login";
    }
    
    // 로그인 폼 이동
    @GetMapping("/login")
    public String loginForm() {
        return "/member/login";
    }
    
    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam("loginId") String loginId,
    					@RequestParam("memberPw") String memberPw,
                        HttpSession session,
                        RedirectAttributes rttr) {

        log.info("로그인 시도 : " + loginId);

        Optional<MemberDTO> result = memberService.login(loginId, memberPw);

        if (result.isPresent()) {
            session.setAttribute("member", result.get());
            log.info("로그인 성공 : " + loginId);
            return "redirect:/main";
        } else {
            rttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "redirect:/member/login";
        }
    }
    
    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main";
    }
    
    // 마이페이지 이동
    @GetMapping("/mypage")
    public String mypage(HttpSession session, RedirectAttributes rttr, Model model) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");

        if (member == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        log.info("마이페이지 접속 : " + member.getLoginId());
        model.addAttribute("member", member);
        return "/member/mypage";
    }
}














