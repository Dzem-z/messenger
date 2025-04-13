package com.project.messenger.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.messenger.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    List<User> findAllByChats_idToken(String token);

    @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT(:prefix, '%')")
    List<User> findAllUsersByUsernamePrefix(String prefix);
}
