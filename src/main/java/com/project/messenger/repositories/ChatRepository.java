package com.project.messenger.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.messenger.entities.Chat;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    
    
    public List<Chat> findAllByMembers_username(String username);

    //public List<Optional<Chat>> findAllBy

    public Optional<Chat> findByIdToken(String idToken);
}
