package com.project.messenger.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.project.messenger.assemblers.FileDtoModelAssembler;
import com.project.messenger.entities.Chat;
import com.project.messenger.entities.User;
import com.project.messenger.exceptions.ChatNotFoundException;
import com.project.messenger.exceptions.StorageException;
import com.project.messenger.models.FileDto;
import com.project.messenger.models.MessageDto;
import com.project.messenger.security.entities.SecurityUser;
import com.project.messenger.services.ChatService;
import com.project.messenger.services.FileSystemStorageService;
import com.project.messenger.services.UserService;



@Controller
public class FileController {
    
    private final FileSystemStorageService storageService;
    private final UserService userService;
    private final ChatService chatService;
    private final FileDtoModelAssembler assembler;

    public FileController(FileSystemStorageService storageService, UserService userService, ChatService chatService, FileDtoModelAssembler assembler) {
        this.storageService = storageService;
        this.chatService = chatService;
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping("/api/files/all/{idToken}")
    public ResponseEntity<CollectionModel<EntityModel<FileDto>>> listFiles(@PathVariable String idToken) throws UserPrincipalNotFoundException {
        /*
         * Lists all files uploaded to chat specified with chatid if the user has access to it (is a member of the chat).
         */

        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findCurrentUser(principal);

        List<EntityModel<FileDto>> files = storageService.findFilesByChatIdTokenAndUser(idToken, user).stream()
            .map(FileDto::new)
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/hal+json"))
            .body(CollectionModel.of(files,
                linkTo(methodOn(FileController.class).listFiles(idToken)).withSelfRel()));
    }

    @GetMapping("/api/files/one/{idToken}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String idToken) throws UserPrincipalNotFoundException {
        /*
         * Serves file described by id specified in the path if the user has access to it (is a member of the chat).
         */

        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findCurrentUser(principal);
        
        Resource file = storageService.loadFileAsResourceByIdTokenAndUser(idToken, user);
             
        if(file == null)
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/api/files/create/{idToken}")
    public ResponseEntity<EntityModel<FileDto>> create(
            @PathVariable String idToken,
            @RequestParam("file") MultipartFile file) throws UserPrincipalNotFoundException {
        /*
         * Saves file in chat with id specified in path.
         */
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findCurrentUser(principal);

        Chat chat = chatService.findChatbyIdTokenAndUser(idToken, user);
        
        if(!chat.getMembers().contains(user))
                throw new ChatNotFoundException("User is not allowed to upload file to: " + chat.toString());
        
        FileDto result = new FileDto(storageService.storeFile(chat, file, user));

        return ResponseEntity
            .created(linkTo(methodOn(FileController.class).serveFile(idToken)).toUri())
            .body(assembler.toModel(result));
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageException exc) {
        
        return ResponseEntity.badRequest().body(exc.getMessage());
    }
    


    


    


}
