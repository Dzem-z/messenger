package com.project.messenger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    @GetMapping("/")
    public String home() {
        return "hello";
    }

    @GetMapping("/data")
    public String getMethodName() {
        return "";
    }
    
}
