package com.project.messenger.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
@Entity
@Table(name = "chats")
@NoArgsConstructor
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @Column(unique=true,
        nullable=false)
    private String idToken;
    
    
    @ManyToMany(fetch = FetchType.EAGER,
        cascade = CascadeType.MERGE)
    @JoinTable(
        name = "chats_users",
        joinColumns = @JoinColumn(name = "chatId"),
        inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<User> members;

    @OneToMany(mappedBy="chat", cascade = CascadeType.REMOVE,
        fetch = FetchType.EAGER)
    private Set<Message> messages;

    public Chat(int id, String name, String idToken, Set<User> members, Set<Message> messages) {
        this.id = id;
        this.name = name;
        this.idToken = idToken;
        this.members = members;
        this.messages = messages;
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

    public Set<User> getMembers() {
        return members;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "Chat{id=" + id + ", name=" + name + ", members=" + members + ", messages=" + messages + "}";
    }

}
