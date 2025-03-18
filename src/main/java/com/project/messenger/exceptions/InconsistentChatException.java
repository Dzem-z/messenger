package com.project.messenger.exceptions;

public class InconsistentChatException extends RuntimeException {
    
    public InconsistentChatException(String message) {
        super("Requested chat contains inconsistent data:" + message);
    }
}
