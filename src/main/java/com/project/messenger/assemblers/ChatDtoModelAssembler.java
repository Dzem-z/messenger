package com.project.messenger.assemblers;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.project.messenger.controllers.ChatController;
import com.project.messenger.controllers.FileController;
import com.project.messenger.controllers.MessageController;
import com.project.messenger.models.ChatDto;

@Component
public class ChatDtoModelAssembler implements RepresentationModelAssembler<ChatDto, EntityModel<ChatDto>> {
    

    @Override
    public EntityModel<ChatDto> toModel(ChatDto chat) {

        try {
            return EntityModel.of(chat,
                linkTo(methodOn(ChatController.class).one(chat.getId())).withSelfRel(),
                linkTo(methodOn(ChatController.class).all("")).withRel("chats"),
                linkTo(methodOn(ChatController.class).leave(chat.getId())).withRel("leave"),
                linkTo(methodOn(MessageController.class).all(chat.getId())).withRel("messages"),
                linkTo(methodOn(FileController.class).listFiles(chat.getIdToken())).withRel("files"));
        } catch (UserPrincipalNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
