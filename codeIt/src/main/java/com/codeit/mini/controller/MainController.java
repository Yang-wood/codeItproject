package com.codeit.mini.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codeit.mini.dto.AdminDTO;
import com.codeit.mini.dto.member.MemberDTO;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@GetMapping("/main")
	public String maingpage() {
		return "main";
	}
	
	@GetMapping("/codeit")
	public String codeit(Model model) {
		
		
		model.addAttribute("currentUri", "/codeit");
		
		return "codeit";
	}
}
