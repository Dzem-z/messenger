package com.project.messenger.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.messenger.StorageProperties;
import com.project.messenger.entities.Chat;
import com.project.messenger.entities.File;
import com.project.messenger.entities.User;
import com.project.messenger.exceptions.StorageException;
import com.project.messenger.repositories.FileRepository;
import com.project.messenger.security.entities.SecurityUser;

@Service
public class FileSystemStorageService {
    
    private final FileRepository fileRepository;
    private final ChatService chatService;
    private final StorageProperties storageProperties;

    public FileSystemStorageService(FileRepository fileRepository, ChatService chatService, StorageProperties storageProperties) {
        this.fileRepository = fileRepository;
        this.chatService = chatService;
        this.storageProperties = storageProperties;
    }

    private Path constructPath(File file) {
        return FileSystems.getDefault().getPath(storageProperties.getLocation(), file.getChat().getIdToken(), String.valueOf(file.getSender().getId()), file.getName());
    }

    public List<File> findFilesByChatIdTokenAndUser(String idToken, User user) throws UserPrincipalNotFoundException {
        chatService.findChatbyIdTokenAndUser(idToken, user); // This is needed for security Check.

        return fileRepository.findAllByChat_idToken(idToken);
    }

    public Resource loadFileAsResourceByIdTokenAndUser(String idToken, User user) {
        File fileDescriptor = fileRepository.findByIdToken(idToken);

        if(!fileDescriptor.getChat().getMembers().contains(user))
            throw new StorageException("User is not authorized to download file: " + fileDescriptor.toString());

        try {
            Path filePath = constructPath(fileDescriptor);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException(
                    "Could not read file: " + fileDescriptor.getName());
            }
        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + fileDescriptor.getName(), e);
        }
    }

    public File storeFile(Chat chat, MultipartFile file, User sender) {
        int fileId = 0;
        String filename = file.getOriginalFilename();
        String idToken = UUID.randomUUID().toString();
        OffsetDateTime dateOfUploading = OffsetDateTime.now();



        File storedFile = new File(
            fileId,
            filename,
            idToken,
            dateOfUploading, 
            chat,
            sender);

        try {
            Path destinationPath = constructPath(storedFile);
            Path parentDir = destinationPath.getParent();
            if(parentDir != null && Files.notExists(parentDir))
                Files.createDirectories(parentDir);

            if(Files.notExists(destinationPath))
                Files.createFile(destinationPath);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationPath,
                    StandardCopyOption.REPLACE_EXISTING);
            }
            return fileRepository.save(storedFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new StorageException("Failed to store file.", e);
        }
    }

}
