package com.project.messenger.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.messenger.entities.Chat;
import com.project.messenger.entities.User;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    
    @Query("SELECT ch FROM Chat ch JOIN ch.members m WHERE m.username = :username AND ch.name LIKE CONCAT(:prefix, '%')")
    List<Chat> findAllByPrefixAndMembers_username(String prefix, String username);

    
    public List<Chat> findAllByMembers_username(String username);

    //public List<Optional<Chat>> findAllBy

    public Optional<Chat> findByIdToken(String idToken);
}
