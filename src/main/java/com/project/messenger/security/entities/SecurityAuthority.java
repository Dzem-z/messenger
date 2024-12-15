package com.project.messenger.security.entities;

import org.springframework.security.core.GrantedAuthority;

import com.project.messenger.entities.Authority;



public class SecurityAuthority implements GrantedAuthority {
    
    private final Authority authority;

    public SecurityAuthority(Authority authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority.getName();
    }

    @Override
    public String toString() {
        return "SecurityAuthority{" + authority.toString() + "}";
    }
}
