package com.project.messenger.models;

import java.util.Set;
import java.util.stream.Collectors;

import com.project.messenger.entities.Chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {
    private int id;

    private String name;

    private String idToken;

    private Set<UserDto> members;

    private Set<MessageDto> messages;

    public ChatDto(Chat chat) {
        this.id = chat.getId();
        this.name = chat.getName();
        this.idToken = chat.getIdToken();
        this.members = chat.getMembers().stream()
            .map(member -> new UserDto(member.getUsername()))
            .collect(Collectors.toSet());
        this.messages = chat.getMessages().stream()
            .map(message -> new MessageDto(message.getId(), message.getContent(), message.getDateOfPosting(), new UserDto(message.getAuthor())))
            .collect(Collectors.toSet());
    }

    public ChatDto(String name) {
        this.name = name;
    }

    public ChatDto() {

    }

    public Set<UserDto> getMembers() {
        return members;
    }

    public int getId() {
        return id;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getName() {
        return name;
    }
}
