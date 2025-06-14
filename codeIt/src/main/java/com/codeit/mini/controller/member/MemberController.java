package com.codeit.mini.controller.member;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.repository.member.IMemberRepository;
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
	private final IMemberRepository memberRepository;
	
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
    
    // 아이디 중복 체크 (Ajax)
    @PostMapping("/checkId")
    @ResponseBody
    public String checkId(@RequestBody Map<String, String> body) {
        String loginId = body.get("loginId");
        boolean exists = memberRepository.findByLoginId(loginId).isPresent();
        return exists ? "duplicate" : "available";
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

        log.info("마이페이지 접속 : " + member.getLoginId());
        model.addAttribute("member", member);
        return "/member/mypage";
    }
    
    // 내 정보 수정 폼
    @GetMapping("/modify")
    public String modifyForm(HttpSession session, Model model, RedirectAttributes rttr) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");

        Optional<MemberDTO> result = memberService.read(member.getMemberId());
        if (result.isPresent()) {
            model.addAttribute("dto", result.get());
            return "/member/modify";
        } else {
            rttr.addFlashAttribute("msg", "회원 정보가 존재하지 않습니다.");
            return "redirect:/member/mypage";
        }
    }

    // 내 정보 수정 처리
    @PostMapping("/modify")
    public String modify(MemberDTO dto, HttpSession session, RedirectAttributes rttr) {
        MemberDTO sessionMember = (MemberDTO) session.getAttribute("member");

        // 세션의 memberId를 강제로 덮어씌움 (보안)
        dto.setMemberId(sessionMember.getMemberId());

        log.info("회원 정보 수정 : " + dto);
        memberService.modify(dto);

        // 수정된 정보를 다시 세션에 반영
        Optional<MemberDTO> updated = memberService.read(dto.getMemberId());
        updated.ifPresent(u -> session.setAttribute("member", u));

        rttr.addFlashAttribute("msg", "회원 정보가 수정되었습니다.");
        return "redirect:/member/mypage";
    }
}














