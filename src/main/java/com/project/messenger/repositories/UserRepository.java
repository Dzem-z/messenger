package com.project.messenger.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.messenger.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findUserByUsername(String username);
}
