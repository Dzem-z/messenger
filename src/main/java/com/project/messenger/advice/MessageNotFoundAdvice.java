package com.project.messenger.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.messenger.exceptions.MessageNotFoundException;

@RestControllerAdvice
public class MessageNotFoundAdvice {
    
    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String messageNotFoundHandler(MessageNotFoundException ex) {
        return ex.getMessage();
    }
}
