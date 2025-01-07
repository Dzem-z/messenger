package com.project.messenger.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.project.messenger.models.UserDto;

@Component
public class UserDtoModelAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {
    
    @Override
    public EntityModel<UserDto> toModel(UserDto userModel) {
        return EntityModel.of(userModel);
    }
}
