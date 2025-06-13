package com.codeit.mini.controller.book;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller("/book")
@RequiredArgsConstructor
public class PageMoveController {
	
	@GetMapping("/search")
	public String searchPage(@RequestParam(defaultValue = "") String keyword,
							 @RequestParam(defaultValue = "all") String type,
							 Model model) {
		model.addAttribute("keyword", keyword);
		model.addAttribute("type", type);
		
		return "searchResult";
	}
	
	
	
}
