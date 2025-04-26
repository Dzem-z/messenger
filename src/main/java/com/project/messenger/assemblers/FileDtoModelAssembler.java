package com.project.messenger.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.project.messenger.controllers.FileController;
import com.project.messenger.models.FileDto;

public class FileDtoModelAssembler implements RepresentationModelAssembler<FileDto, EntityModel<FileDto>> {
    
    @Override
    public EntityModel<FileDto> toModel(FileDto file) {
        try {
            return EntityModel.of(file,
                linkTo(methodOn(FileController.class).serveFile(file.getIdToken())).withRel("downloadFile"),
                linkTo(methodOn(FileController.class).listFiles(file.getChat().getIdToken())).withRel("files"));
        } catch (UserPrincipalNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
