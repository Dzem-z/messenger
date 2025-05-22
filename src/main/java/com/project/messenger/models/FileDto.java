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

    private String content;

    private String idToken;

    private OffsetDateTime dateOfPosting;

    private ChatDto chat;

    private UserDto author;

    public FileDto(File file) {
        this.content = file.getName();
        this.idToken = file.getIdToken();
        this.dateOfPosting = file.getDateOfPosting();
        this.chat = new ChatDto(file.getChat());
        this.author = new UserDto(file.getSender());
    }

    public FileDto(String name, String idToken, OffsetDateTime dateOfPosting, UserDto sender) {
        this.content = name;
        this.idToken = idToken;
        this.dateOfPosting = dateOfPosting;
        this.author = sender;
    }

    public String getName() {
        return content;
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
        return author;
    }
}
