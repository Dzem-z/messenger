package com.project.messenger.services;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.messenger.entities.Chat;
import com.project.messenger.entities.File;
import com.project.messenger.repositories.FileRepository;
import com.project.messenger.security.entities.SecurityUser;

@Service
public class FileSystemStorageService {
    
    private final FileRepository fileRepository;
    private final ChatService chatService;

    public FileSystemStorageService(FileRepository fileRepository, ChatService chatService) {
        this.fileRepository = fileRepository;
        this.chatService = chatService;
    }

    public List<File> findFilesByChatIdTokenAndAuthorize(String idToken, SecurityUser principal) throws UserPrincipalNotFoundException {
        chatService.findChatbyIdTokenAndAuthorize(idToken, principal); // This is needed for security Check.

        return fileRepository.findAllByChat_idToken(idToken);
    }


}
