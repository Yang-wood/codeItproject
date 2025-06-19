package com.codeit.mini.controller.admin;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/admin")
public class AdminSessionCheckController {

    @GetMapping("/check")
    public ResponseEntity<?> checkAdminLogin(HttpSession session) {
        Object admin = session.getAttribute("admin");

        if (admin != null) {
            return ResponseEntity.ok(Map.of("loggedIn", true));
        } else {
            return ResponseEntity.ok(Map.of("loggedIn", false));
        }
    }
}
