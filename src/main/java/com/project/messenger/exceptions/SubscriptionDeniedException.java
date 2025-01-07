package com.project.messenger.exceptions;

public class SubscriptionDeniedException extends RuntimeException {
    
    public SubscriptionDeniedException(String message) {
        super(message);
    }
}
