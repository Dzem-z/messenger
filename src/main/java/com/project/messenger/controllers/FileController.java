package com.project.messenger.controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;



@Controller
public class FileController {
    
    private final StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/api/files/{chatid}")
    public CollectionModel<EntityModel<FileDto>> listFiles(@PathVariable String chatid) {
        /*
         * Lists all files uploaded to chat specified with chatid if the user has access to it (is a member of the chat).
         */
    }

    @GetMapping("/api/files/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String id) {
            /*
             * Serves file described by id specified in the path if the user has access to it (is a member of the chat).
             */
        
    }

    @PostMapping("/api/files/{id}")
    public ResponseEntity<?> create(
        @PathVariable String id,
        @RequestParam("file") MultipartFile file) {
        
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        
    }
    


    


    


}
