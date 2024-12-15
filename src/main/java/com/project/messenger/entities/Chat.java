package com.project.messenger.entities;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chats")
@AllArgsConstructor
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue
    private int id;

    private String name;
    
    @ManyToMany(mappedBy = "chats")
    private Set<User> members;

}
