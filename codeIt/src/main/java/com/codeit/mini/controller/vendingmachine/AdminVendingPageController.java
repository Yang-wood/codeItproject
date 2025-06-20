package com.codeit.mini.controller.vendingmachine;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codeit.mini.dto.vending.MachineItemDTO;
import com.codeit.mini.dto.vending.VendingItemDTO;
import com.codeit.mini.dto.vending.VendingMachineDTO;
import com.codeit.mini.service.vending.IMachineItemService;
import com.codeit.mini.service.vending.IVendingItemService;
import com.codeit.mini.service.vending.IVendingMachineService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/vending")
@RequiredArgsConstructor
public class AdminVendingPageController {
	
	private final IVendingMachineService vendingMachineService;
	private final IMachineItemService machineItemService;
	private final IVendingItemService itemService;
	
	
    @GetMapping("/list")
    public String vendingPage(Model model) {
        return "admin/vending/list";
    }
    @GetMapping("/register")
    public String registerPage(Model model) {
        return "admin/vending/register";
    }
    @GetMapping("/{machineId}")
    public String vendingDetailPage(@PathVariable("machineId") Long machineId, Model model) {
        VendingMachineDTO vending = vendingMachineService.findVendingMachineById(machineId)
                .orElseThrow(() -> new RuntimeException("자판기 정보가 없습니다."));

        List<MachineItemDTO> machineItems = machineItemService.findAllItemsByMachineId(machineId);

        for (MachineItemDTO item : machineItems) {
            System.out.println("🧾 item = " + item.getName() + ", type = " + item.getItemType());
        }
        
        List<VendingItemDTO> converted = machineItems.stream()
                .map(VendingItemDTO::from)
                .collect(Collectors.toList());

        vending.setItems(converted); // 여기서 타입 맞음

        model.addAttribute("vending", vending);
        return "admin/vending/detail";
    }
    
    @GetMapping("/itemlist")
    public String itemPage(Model model) {
        return "admin/vending/itemlist";
    }
    
    @GetMapping("/itemregister")
    public String itemregisterPage(Model model) {
        return "admin/vending/itemregister";
    }
    
    @GetMapping("/item/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {
        VendingItemDTO item = itemService.findVendingItemById(itemId)
            .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        
        model.addAttribute("item", item);
        return "admin/vending/item/detail";
    }
    
    @GetMapping("/item/{itemId}/edit")
    public String itemEdit(@PathVariable("itemId") Long itemId, Model model) {
        VendingItemDTO item = itemService.findVendingItemById(itemId)
            .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        
        model.addAttribute("item", item);
        return "admin/vending/item/edit";
    }
    
}
