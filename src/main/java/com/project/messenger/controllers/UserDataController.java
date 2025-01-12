package com.project.messenger.controllers;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.messenger.assemblers.UserDtoModelAssembler;
import com.project.messenger.models.UserDto;
import com.project.messenger.security.entities.SecurityUser;
import com.project.messenger.services.UserService;



@RestController
public class UserDataController {
    
    private final UserService userService;
    private final UserDtoModelAssembler assembler;

    public UserDataController(UserService userService, UserDtoModelAssembler assembler) {
        this.assembler = assembler;
        this.userService = userService;
    }

    @GetMapping("/api/user")
    public EntityModel<UserDto> getUserInfo() throws UserPrincipalNotFoundException {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return assembler.toModel(new UserDto(userService.findCurrentUser(principal)));
    }

    @GetMapping("/api/users")
    public CollectionModel<EntityModel<UserDto>> findUsersByPrefix(@RequestParam(value="prefix", defaultValue="") String prefix) {
        List<EntityModel<UserDto>> users = userService.findUsersByPrefix(prefix).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        System.out.println(userService.findUsersByPrefix(prefix));
        return CollectionModel.of(users,
            linkTo(methodOn(UserDataController.class).findUsersByPrefix(prefix)).withSelfRel());
    }
    

}
