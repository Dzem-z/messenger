package com.project.messenger.advice;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class MultipartExceptionAdvice {
    
    
    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String MultipartExceptionHandler(Model model) {
        model.addAttribute("errorMessage", "Multipart exception: wrong HTTP request.");
        return "error/multipartError";
    }
}
