package com.project.messenger.entities;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Setter;

@Entity
@Table(name = "authorities")
@Setter
public class Authority {
    
    @Id
    @GeneratedValue
    private int id;

    private String name;


    @ManyToMany(mappedBy = "authorities")
    private Set<User> Users;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Authority{id=" + id + ", name= \"" + name + "\"}";
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;

        if(!(o instanceof Authority))
            return false;
        
        Authority authority = (Authority) o;
        return id == authority.id && name.equals(authority.name);
    }
}
