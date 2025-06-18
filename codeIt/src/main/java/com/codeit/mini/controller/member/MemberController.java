package com.codeit.mini.controller.member;

import java.util.HashMap;
import java.util.Map;


import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.codeit.mini.entity.member.EmailAuthEntity;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.repository.member.EmailAuthRepository;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.service.member.EmailService;
import com.codeit.mini.service.member.IMemberService;

import jakarta.servlet.http.HttpServletRequest;
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
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final EmailAuthRepository emailAuthRepository;
	
	// 회원 가입 페이지 이동
    @GetMapping("/register")
    public String registerForm() {
        return "/member/register";
    }
    
    // 회원 가입 처리
    @PostMapping("/register")
    public String register(MemberDTO dto, RedirectAttributes rttr) {
        log.info("회원 가입 요청 : " + dto);
        memberService.register(dto);
        rttr.addFlashAttribute("msg", "회원 가입이 완료되었습니다.");
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
            return "redirect:/codeit";
        } else {
            Optional<MemberEntity> entity = memberRepository.findByLoginId(loginId);
            if (entity.isPresent() && entity.get().getStatus() == 2) {
                rttr.addFlashAttribute("msg", "탈퇴한 회원입니다.");
            } else {
                rttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 올바르지 않습니다.");
            }
            return "redirect:/member/login";
        }
    }
    
    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/codeit";
    }
    
    // 마이페이지 이동
    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model, RedirectAttributes rttr, HttpServletRequest request) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");

        Optional<MemberDTO> refreshed = memberService.read(member.getMemberId());

        if (refreshed.isPresent()) {
            MemberDTO latest = refreshed.get();
            session.setAttribute("member", latest);  // 세션 갱신
            model.addAttribute("member", latest);    // 화면에도 반영
            
            model.addAttribute("rentCount", memberService.getRentCount(latest.getMemberId())); //내 서재 개수
            model.addAttribute("wishCount", memberService.getWishCount(latest.getMemberId())); //위시리스트 개수
            
            model.addAttribute("currentUri", request.getRequestURI());
            
            return "/member/mypage";
        } else {
            rttr.addFlashAttribute("msg", "회원 정보를 불러올 수 없습니다.");
            return "redirect:/codeit";
        }
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
    
    @PostMapping("/delete")
    public String deleteMember(HttpSession session, RedirectAttributes rttr) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");

        // Soft Delete 수행
        memberService.delete(member.getMemberId());

        session.invalidate(); // 세션 종료 (로그아웃)
        rttr.addFlashAttribute("msg", "회원 탈퇴가 완료되었습니다.");

        return "redirect:/codeit";
    }
    
    @PostMapping("/ajax/find-id")
    @ResponseBody
    public Map<String, String> findIdAjax(@RequestBody Map<String, String> body) {
        String name = body.get("memberName");
        String email = body.get("memberEmail");

        Optional<MemberEntity> result = memberRepository.findByMemberNameAndMemberEmail(name, email);
        Map<String, String> response = new HashMap<>();

        if (result.isPresent()) {
            response.put("result", "success");
            response.put("loginId", result.get().getLoginId());
        } else {
            response.put("result", "fail");
        }

        return response;
    }
    
    @PostMapping("/ajax/find-pw")
    @ResponseBody
    public Map<String, String> findPassword(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        String email = request.get("memberEmail");

        Map<String, String> result = new HashMap<>();

        Optional<MemberEntity> optional = memberRepository.findByLoginId(loginId);

        if (optional.isEmpty() || !optional.get().getMemberEmail().equals(email)) {
            result.put("result", "fail");
            result.put("msg", "일치하는 회원이 없습니다.");
            return result;
        }

        MemberEntity entity = optional.get();

        // ✅ 임시 비밀번호 생성 및 저장
        String tempPw = UUID.randomUUID().toString().substring(0, 8);
        String encoded = passwordEncoder.encode(tempPw);
        entity.changepw(encoded);
        memberRepository.save(entity);

        // ✅ 이메일 발송
        emailService.sendTempPassword(email, tempPw);

        result.put("result", "success");
        result.put("msg", "임시 비밀번호가 이메일로 전송되었습니다.");
        return result;
    }
    
    @PostMapping("/ajax/update-name")
    @ResponseBody
    public Map<String, String> updateName(@RequestBody Map<String, String> body, HttpSession session) {
        
    	Map<String, String> response = new HashMap<>();
        MemberDTO sessionMember = (MemberDTO) session.getAttribute("member");

        if (sessionMember == null) {
            response.put("result", "fail");
            response.put("msg", "로그인이 필요합니다.");
            return response;
        }

        String newName = body.get("memberName");

        // 이름 업데이트 수행
        sessionMember.setMemberName(newName);
        memberService.modify(sessionMember);

        // 세션 갱신
        Optional<MemberDTO> updated = memberService.read(sessionMember.getMemberId());
        updated.ifPresent(u -> session.setAttribute("member", u));

        response.put("result", "success");
        response.put("msg", "이름이 성공적으로 변경되었습니다.");
        return response;
    }
    
    @PostMapping("/ajax/update-email")
    @ResponseBody
    public Map<String, String> updateEmail(@RequestBody Map<String, String> body, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        MemberDTO sessionMember = (MemberDTO) session.getAttribute("member");

        if (sessionMember == null) {
            response.put("result", "fail");
            response.put("msg", "로그인이 필요합니다.");
            return response;
        }

        String newEmail = body.get("newEmail");

        // 1. 인증된 이메일인지 확인
        Optional<EmailAuthEntity> result = emailAuthRepository.findById(newEmail);
        if (result.isEmpty() || !result.get().isVerified()) {
            response.put("result", "fail");
            response.put("msg", "이메일 인증이 완료되지 않았습니다.");
            return response;
        }

        // 2. 이메일 변경
        sessionMember.setMemberEmail(newEmail);
        memberService.modify(sessionMember);

        // 3. 세션 갱신
        Optional<MemberDTO> updated = memberService.read(sessionMember.getMemberId());
        updated.ifPresent(u -> session.setAttribute("member", u));

        // 4. 인증 정보 삭제 (보안 차원)
        emailAuthRepository.deleteById(newEmail);

        response.put("result", "success");
        response.put("msg", "이메일이 성공적으로 변경되었습니다.");
        return response;
    }
    
    @PostMapping("/ajax/update-password")
    @ResponseBody
    public Map<String, String> updatePassword(@RequestBody Map<String, String> body, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        MemberDTO sessionMember = (MemberDTO) session.getAttribute("member");

        if (sessionMember == null) {
            response.put("result", "fail");
            response.put("msg", "로그인이 필요합니다.");
            return response;
        }

        String currentPw = body.get("currentPw");
        String newPw = body.get("newPw");

        // 현재 비밀번호 확인
        Optional<MemberEntity> entityOpt = memberRepository.findById(sessionMember.getMemberId());
        if (entityOpt.isEmpty()) {
            response.put("result", "fail");
            response.put("msg", "회원 정보가 존재하지 않습니다.");
            return response;
        }

        MemberEntity entity = entityOpt.get();
        if (!passwordEncoder.matches(currentPw, entity.getMemberPw())) {
            response.put("result", "fail");
            response.put("msg", "현재 비밀번호가 올바르지 않습니다.");
            return response;
        }

        // 새 비밀번호 암호화 및 저장
        entity.setMemberPw(passwordEncoder.encode(newPw));
        memberRepository.save(entity);

        response.put("result", "success");
        response.put("msg", "비밀번호가 성공적으로 변경되었습니다.");
        return response;
    }
}














