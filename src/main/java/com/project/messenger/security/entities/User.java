package com.project.messenger.security.entities;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Setter;


@Entity
@Table(name = "users")
@Setter
public class User {
    
    @Id
    @GeneratedValue
    private int id;

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="users_authorities",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "authorityId"))
    private Set<Authority> authorities;

    public User() {
        username = null;
        password = null;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    Set<Authority> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "User{id="+ id + ", username=\"" + username + "\", password=\"" + password +"\"}";
    }
}
