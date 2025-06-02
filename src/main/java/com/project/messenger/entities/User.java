package com.project.messenger.entities;

import java.util.Objects;
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
import lombok.Setter;

@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    
    @Id
    @GeneratedValue
    private int id;

    @Column(unique=true,
        nullable=false)
    private String username;

    private String password;

    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_authorities",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "authorityId"))
    private Set<Authority> authorities;

    @ManyToMany(mappedBy="members",
        cascade = CascadeType.REMOVE,
        fetch = FetchType.EAGER)
    private Set<Chat> chats;

    
    @OneToMany(mappedBy="author",
        fetch = FetchType.EAGER)
    private Set<Message> messages;

    public User() {
        username = null;
        password = null;
        email = null;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username=\""
                + username + "\", password=\""
                + password + "\", email=\""
                + email + "\"}";
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;

        if(!(o instanceof User))
            return false;
        
        User user = (User) o;
        return id == user.id && username.equals(user.username) &&
            password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
