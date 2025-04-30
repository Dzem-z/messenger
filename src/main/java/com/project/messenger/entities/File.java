package com.project.messenger.entities;

import java.time.OffsetDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
public class File {
    
    @Id
    @GeneratedValue
    private int id;

    private String name;

    private OffsetDateTime dateOfPosting;

    @Column(unique=true,
        nullable=false)
    private String idToken;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
        name = "user_id",
        nullable = false)
    private User sender;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
        name = "chat_id",
        nullable = false)
    private Chat chat;

    public File(int id, String name, String idToken, OffsetDateTime dateOfPosting, Chat chat, User sender) {
        this.id = id;
        this.name = name;
        this.idToken = idToken;
        this.dateOfPosting = dateOfPosting;
        this.chat = chat;
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public Chat getChat() {
        return chat;
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

    public User getSender() {
        return sender;
    }
}
