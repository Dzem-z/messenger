package com.project.messenger.security.entities;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.messenger.entities.User;


public class SecurityUser implements UserDetails {

    private final User user;


    
    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities()
        .stream()
        .map(SecurityAuthority::new)
        .collect(Collectors.toList());
    }

    

    
    
}
