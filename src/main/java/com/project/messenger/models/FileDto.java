package com.project.messenger.models;

import java.time.OffsetDateTime;

import com.project.messenger.entities.File;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FileDto {
    private int id;

    private String name;

    private OffsetDateTime dateOfPosting;

    private ChatDto chat;

    private UserDto sender;

    public FileDto(File file) {
        this.id = file.getId();
        this.name = file.getName();
        this.dateOfPosting = file.getDateOfPosting();
        this.chat = new ChatDto(file.getChat());
        this.sender = new UserDto(file.getSender());
    }

    public FileDto(int id, String name, OffsetDateTime dateOfPosting, UserDto sender) {
        this.id = id;
        this.name = name;
        this.dateOfPosting = dateOfPosting;
        this.sender = sender;
    }
}
