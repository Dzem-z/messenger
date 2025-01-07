package com.project.messenger.advice;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.project.messenger.exceptions.ChatNotFoundException;

@ControllerAdvice
public class SubscriptionDeniedHandlerAdvice {
    
    @ExceptionHandler(ChatNotFoundException.class)
    String SubscriptionDeniedHandler(ChatNotFoundException ex) {
        return "Not allowed to subscribe to current chat";
    }

    @MessageExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e) {
        return "An error occurred: " + e.getMessage();
    }
}
