package com.project.messenger;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    @GetMapping("/")
    public String home() {
        var u = SecurityContextHolder.getContext().getAuthentication();
        u.getAuthorities().forEach(a -> System.out.println(a));
        return "hello";
    }

    @GetMapping("/data")
    public String getMethodName() {
        return "";
    }
    
}
