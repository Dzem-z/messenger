package com.project.messenger.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.messenger.exceptions.ChatNotFoundException;

@RestControllerAdvice
public class ChatNotFoundAdvice {
    
    @ExceptionHandler(ChatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String chatNotFoundHandler(ChatNotFoundException ex) {
        return ex.getMessage();
    }
}
