package com.project.messenger.security.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.project.messenger.security.entities.SecurityUser;
import com.project.messenger.security.entities.User;
import com.project.messenger.security.repositories.UserRepository;


public class JpaUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> u = userRepository.findUserByUsername(username);
        
        return u.map(SecurityUser::new)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));
    }
}
