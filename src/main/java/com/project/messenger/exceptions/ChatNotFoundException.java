package com.project.messenger.exceptions;

public class ChatNotFoundException extends RuntimeException {
    
    public ChatNotFoundException(String message) {
        super("Could not find chat. " + message);
    }
}
