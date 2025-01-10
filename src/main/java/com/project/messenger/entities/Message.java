package com.project.messenger.entities;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
@Entity
@Table(name = "messages")
@NoArgsConstructor
@Getter
@Setter
public class Message {
    
    @Id
    @GeneratedValue
    private int id;

    private String content;

    private OffsetDateTime dateOfPosting;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinTable(
        name = "users_messages",
        joinColumns = @JoinColumn(name = "messageId"),
        inverseJoinColumns = @JoinColumn(name = "chatId"))
    private User author;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinTable(
        name = "chats_messages",
        joinColumns = @JoinColumn(name = "messageId"),
        inverseJoinColumns = @JoinColumn(name = "chatId"))
    private Chat chat;

    public Message(int id, String content, OffsetDateTime dateOfPosting, Chat chat, User author) {
        this.id = id;
        this.content = content;
        this.dateOfPosting = dateOfPosting;
        this.chat = chat;
        this.author = author;
    };

    public int getId() {
        return id;
    }

    public Chat getChat() {
        return chat;
    }

    public String getContent() {
        return content;
    }

    public OffsetDateTime getDateOfPosting() {
        return dateOfPosting;
    }

    public User getAuthor() {
        return author;
    }
}
