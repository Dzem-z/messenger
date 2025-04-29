package com.project.messenger.exceptions;

public class MessageNotFoundException extends RuntimeException {
    
    public MessageNotFoundException(String message) {
        super("Could not find message:" + message);
    }
}
