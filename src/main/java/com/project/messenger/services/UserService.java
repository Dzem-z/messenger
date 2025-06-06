package com.project.messenger.services;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.messenger.entities.User;
import com.project.messenger.models.UserDto;
import com.project.messenger.repositories.UserRepository;
import com.project.messenger.security.entities.SecurityUser;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findCurrentUser(SecurityUser principal) throws UserPrincipalNotFoundException {
        return userRepository.findUserByUsername(principal.getUsername())
            .orElseThrow(() -> new UserPrincipalNotFoundException("Currently logged user doesn't exist"));
    }

    public List<UserDto> findUsersByPrefix(String prefix) { //refactor
        return userRepository.findAllUsersByUsernamePrefix(prefix).stream()
            .map(user -> new UserDto(user.getUsername()))
            .collect(Collectors.toList());
    }

    public User registerUser(User user) {
        System.out.println(userRepository.findUserByUsername(user.getUsername()));
        if(userRepository.findUserByUsername(user.getUsername()).isPresent())
            throw new BadCredentialsException("User is already present.");

        if(userRepository.findUserByEmail(user.getEmail()).isPresent())
            throw new BadCredentialsException("Email is already present.");

        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }
}
