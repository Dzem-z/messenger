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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.messenger.assemblers.ChatDtoModelAssembler;
import com.project.messenger.entities.Chat;
import com.project.messenger.entities.User;
import com.project.messenger.models.ChatDto;
import com.project.messenger.security.entities.SecurityUser;
import com.project.messenger.services.ChatService;
import com.project.messenger.services.UserService;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final ChatDtoModelAssembler assembler;

    public ChatController(ChatService chatService, UserService userService, ChatDtoModelAssembler assembler) {
        this.userService = userService;
        this.chatService = chatService;
        this.assembler = assembler;
    }

    
    @GetMapping("/api/chats")
    public CollectionModel<EntityModel<ChatDto>> all(@RequestParam(value="prefix", defaultValue="") String prefix) throws UserPrincipalNotFoundException {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("principal: " + principal);

        User user = userService.findCurrentUser(principal);

        List<EntityModel<ChatDto>> chats = chatService.findAllPublicChatsWithPrefixByUser(prefix, user).stream()
            .map(ChatDto::new)
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(chats,
            linkTo(methodOn(ChatController.class).all(prefix)).withSelfRel(),
            linkTo(methodOn(ChatController.class).create(new ChatDto())).withRel("create"));
    }

    @GetMapping("/api/chats/{id}")
    public EntityModel<ChatDto> one(@PathVariable int id) {
        ChatDto chat = new ChatDto(chatService.findChatbyId(id));

        return assembler.toModel(chat);
    }

    //@CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/chats/create")
    public ResponseEntity<EntityModel<ChatDto>> create(@RequestBody ChatDto chat) {
        System.out.println("submitted: " + chat);
        ChatDto newChat = new ChatDto(chatService.createChat(chat));

        return ResponseEntity
            .created(linkTo(methodOn(ChatController.class).one(newChat.getId())).toUri())
            .body(assembler.toModel(newChat));
    }

    @DeleteMapping("/api/chats/{id}/leave")
    public ResponseEntity<?> leave(@PathVariable int id) throws UserPrincipalNotFoundException {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findCurrentUser(principal);
        Chat requestedChat = chatService.findChatbyId(id);

        chatService.removeUserFromChat(user, requestedChat);

        return ResponseEntity.ok("");
    }

    

}
