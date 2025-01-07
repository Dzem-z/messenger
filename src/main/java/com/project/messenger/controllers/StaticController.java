package com.project.messenger.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class StaticController {
    
    @GetMapping("/")
    public String index() {
        return "forward:/static/index.html";
    }
}
