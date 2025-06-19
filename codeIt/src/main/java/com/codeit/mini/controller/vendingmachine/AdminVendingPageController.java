package com.codeit.mini.controller.vendingmachine;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/vending")
public class AdminVendingPageController {
	
    @GetMapping("/list")
    public String vendingPage(Model model) {
        return "admin/vending/list";
    }
    
}
