package com.project.messenger.controllers;

import com.project.messenger.services.PasswordResetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "for";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @RequestParam("email") String email,
            RedirectAttributes redirectAttributes) {
        passwordResetService.startPasswordResetProcess(email);
        redirectAttributes.addAttribute("success", true);
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password/{token}")
    public String resetPassword(@PathVariable String token) {
        if (passwordResetService.verifyPasswordResetToken(token)) {
            return "res";
        }
        return "Error";
    }

    @PostMapping("/reset-password/{token}")
    public String resetPassword(
            @PathVariable String token,
            @RequestParam String password) {
        passwordResetService.resetPassword(token, password);
        return "redirect:/login";
    }
}
