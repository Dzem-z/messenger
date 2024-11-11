package com.project.messenger;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String nick;

    protected User() {}

    public User(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return String.format(
            "Customer[id=%d, nick='%s']",
            id, nick);
    }

    public Long getId(){
        return id;
    }

    public String getNick(){
        return nick;
    }

}
