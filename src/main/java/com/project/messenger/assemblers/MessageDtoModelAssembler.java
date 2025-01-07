package com.project.messenger.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.project.messenger.controllers.MessageController;
import com.project.messenger.entities.Message;
import com.project.messenger.models.MessageDto;


@Component
public class MessageDtoModelAssembler implements RepresentationModelAssembler<MessageDto, EntityModel<MessageDto>> {
    
    @Override
    public EntityModel<MessageDto> toModel(MessageDto message) {
        return EntityModel.of(message,
            linkTo(methodOn(MessageController.class).one(message.getId())).withSelfRel(),
            linkTo(methodOn(MessageController.class).all(message.getChat().getId())).withRel("Messages"));
    }
}
