package com.project.messenger.models;

import java.util.Set;
import java.util.stream.Collectors;

import com.project.messenger.entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {

    private int Id;
    private final String username;

    //private Set<Authority> authorities;
    private Set<ChatDto> chats;

    public UserDto(User user) {
        if(user == null) {
            this.Id = 0;
            this.username = null;
            this.chats = null;
        } else {
            this.Id = user.getId();
            this.username = user.getUsername();
            //this.authorities = user.getAuthorities();
            this.chats = user.getChats().stream()
                .map(chat -> new ChatDto(chat.getName()))
                .collect(Collectors.toSet());
        }
    }

    public UserDto(String username) {
        this.username = username;

    }


    public String getUsername() {
        return username;
    }
}
