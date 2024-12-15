package com.project.messenger.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.messenger.security.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findUserByUsername(String username);
}
