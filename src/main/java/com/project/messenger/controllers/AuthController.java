package com.project.messenger.controllers;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.messenger.entities.User;
import com.project.messenger.services.UserService;


@Controller
public class AuthController {

    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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
    public String getMethodName(@ModelAttribute("user") User user,
        BindingResult result,
        RedirectAttributes attributes) {
        System.out.println("submitted");

        try {
            userService.registerUser(user);
        } catch (BadCredentialsException e) {
            attributes.addAttribute("error", true);
            return "redirect:/register";
        }

        attributes.addAttribute("success", true);
        return "redirect:/register";
    }

    /*@GetMapping("/login")
    public String login() {
        return "login";
    }*/
    
}
