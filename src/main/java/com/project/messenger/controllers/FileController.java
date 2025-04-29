package com.project.messenger.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.project.messenger.assemblers.FileDtoModelAssembler;
import com.project.messenger.entities.User;
import com.project.messenger.exceptions.StorageFileNotFoundException;
import com.project.messenger.models.FileDto;
import com.project.messenger.security.entities.SecurityUser;
import com.project.messenger.services.FileSystemStorageService;
import com.project.messenger.services.UserService;



@Controller
public class FileController {
    
    private final FileSystemStorageService storageService;
    private final UserService userService;
    private final FileDtoModelAssembler assembler;

    public FileController(FileSystemStorageService storageService, UserService userService, FileDtoModelAssembler assembler) {
        this.storageService = storageService;
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping("/api/files/one/{idToken}")
    public CollectionModel<EntityModel<FileDto>> listFiles(@PathVariable String idToken) throws UserPrincipalNotFoundException {
        /*
         * Lists all files uploaded to chat specified with chatid if the user has access to it (is a member of the chat).
         */

        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findCurrentUser(principal);

        List<EntityModel<FileDto>> files = storageService.findFilesByChatIdTokenAndUser(idToken, user).stream()
            .map(FileDto::new)
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(files,
            linkTo(methodOn(FileController.class).listFiles(idToken)).withSelfRel());
    }

    @GetMapping("/api/files/all/{idToken}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String idToken) throws UserPrincipalNotFoundException {
        /*
         * Serves file described by id specified in the path if the user has access to it (is a member of the chat).
         */

        /*SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findCurrentUser(principal);
        
        Resource file = storageService.loadFileByIdTokenAndUser(idToken, user);*/
             
        return null;
    }

    @PostMapping("/api/files/create/{idToken}")
    public ResponseEntity<?> create(
        @PathVariable String idToken,
        @RequestParam("file") MultipartFile file) {
            /*
             * Saves file in chat with id specified in path.
             */
        return null;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        
        return null;
    }
    


    


    


}
