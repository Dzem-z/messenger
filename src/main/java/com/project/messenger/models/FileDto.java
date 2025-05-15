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

    private String name;

    private String idToken;

    private OffsetDateTime dateOfPosting;

    private ChatDto chat;

    private UserDto sender;

    public FileDto(File file) {
        this.name = file.getName();
        this.idToken = file.getIdToken();
        this.dateOfPosting = file.getDateOfPosting();
        this.chat = new ChatDto(file.getChat());
        this.sender = new UserDto(file.getSender());
    }

    public FileDto(String name, String idToken, OffsetDateTime dateOfPosting, UserDto sender) {
        this.name = name;
        this.idToken = idToken;
        this.dateOfPosting = dateOfPosting;
        this.sender = sender;
    }

    public String getName() {
        return name;
    }

    public String getIdToken() {
        return idToken;
    }

    public OffsetDateTime getDateOfPosting() {
        return dateOfPosting;
    }

    public ChatDto getChat() {
        return chat;
    }

    public UserDto getSender() {
        return sender;
    }
}
