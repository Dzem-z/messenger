package com.project.messenger.exceptions;

public class StorageFileNotFoundException extends RuntimeException {
    
    public StorageFileNotFoundException(String message) {
        super("Could not find file or User is not authorized to download it. " + message);
    }
}
