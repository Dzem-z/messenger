package com.project.messenger.controllers;

import com.project.messenger.services.VerificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class VerificationController {
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping("/verify/{token}")
    public String verify(@PathVariable String token) {
        var result = verificationService.verifyUser(token);
//        var result = true;
        if (result)
            return "mail-suc";
        return "mail-err";
    }
}
