package com.project.messenger.models;

import java.time.OffsetDateTime;

import com.project.messenger.entities.Message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class MessageDto {
    private int id;

    private String content;

    private OffsetDateTime dateOfPosting;

    private ChatDto chat;

    private UserDto author;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.dateOfPosting = message.getDateOfPosting();
        this.chat = new ChatDto(message.getChat());
        this.author = new UserDto(message.getAuthor());
    }

    public MessageDto(int id, String content, OffsetDateTime dateOfPosting, UserDto author) {
        this.id = id;
        this.content = content;
        this.dateOfPosting = dateOfPosting;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public ChatDto getChat() {
        return chat;
    }

    public OffsetDateTime getDateOfPosting() {
        return dateOfPosting;
    }

    public String getContent() {
        return content;
    }

}
