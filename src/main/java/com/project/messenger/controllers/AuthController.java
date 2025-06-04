package com.project.messenger.controllers;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import com.project.messenger.services.VerificationService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.messenger.entities.User;
import com.project.messenger.security.entities.SecurityUser;
import com.project.messenger.services.UserService;


@Controller
public class AuthController {

    UserService userService;
    VerificationService verificationService;

    public AuthController(UserService userService, VerificationService verificationService) {
        this.userService = userService;
        this.verificationService = verificationService;
    }


    @GetMapping("/login")
    public String login() {
        return "log";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "reg";
    }

    @PostMapping("/register/save")
    public String registerSave(@ModelAttribute("user") User user,
        BindingResult result,
        RedirectAttributes attributes) {
        System.out.println("submitted");

        try {
            userService.registerUser(user);
        } catch (BadCredentialsException e) {
            attributes.addAttribute("error", true);
            return "redirect:/register";
        }

        verificationService.startVerificationProcess(user);
        attributes.addAttribute("success", true);
        return "redirect:/register";
    }
    
    @DeleteMapping("/deleteAccount")
    public String deleteAccount() throws UserPrincipalNotFoundException {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.findCurrentUser(principal);
        userService.deleteUser(user);


        return "redirect:/logout";
    }

}
