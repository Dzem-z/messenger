package com.project.messenger.controllers;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.messenger.assemblers.MessageDtoModelAssembler;
import com.project.messenger.entities.User;
import com.project.messenger.models.MessageDto;
import com.project.messenger.security.entities.SecurityUser;
import com.project.messenger.services.MessageService;
import com.project.messenger.services.UserService;



@RestController
public class MessageController {
    
    private final MessageService messageService;
    private final UserService userService;
    private final MessageDtoModelAssembler assembler;

    public MessageController(MessageService messageService, UserService userService, MessageDtoModelAssembler assembler) {
        this.messageService = messageService;
        this.userService = userService;
        this.assembler = assembler;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/message/{id}")
    public EntityModel<MessageDto> one(@PathVariable int id) throws UserPrincipalNotFoundException {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findCurrentUser(principal);

        return assembler.toModel(new MessageDto(messageService.findById(id, user)));
    }
    
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/messages/{id}")
    public ResponseEntity<CollectionModel<EntityModel<MessageDto>>> all(@PathVariable int id) throws UserPrincipalNotFoundException {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findCurrentUser(principal);
        

        List<EntityModel<MessageDto>> messages = messageService.findAllByChatId(id, user).stream()
            .map(MessageDto::new)
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return ResponseEntity.ok()
            .body(CollectionModel.of(messages,
                linkTo(methodOn(MessageController.class).all(id)).withSelfRel()));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/messages/{id}")
    public ResponseEntity<EntityModel<MessageDto>> create(@PathVariable int id, @RequestBody MessageDto message) throws UserPrincipalNotFoundException {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findCurrentUser(principal);

        MessageDto newMessage = new MessageDto(messageService.saveMessage(id, message, user));

        return ResponseEntity
            .created(linkTo(methodOn(MessageController.class).one(newMessage.getId())).toUri())
            .body(assembler.toModel(newMessage));
    }

    
    
}
