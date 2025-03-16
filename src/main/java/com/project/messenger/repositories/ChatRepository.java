package com.project.messenger.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.messenger.entities.Chat;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    
    @Query("SELECT ch FROM Chat ch JOIN ch.members m WHERE m.username = :username AND ch.name LIKE CONCAT(:prefix, '%') AND ch.isPrivate = false")
    List<Chat> findAllPublicByPrefixAndMembers_username(String prefix, String username);

    @Query("SELECT ch from Chat ch JOIN ch.members m WHERE m.username = :username AND ch.isPrivate = false")
    public List<Chat> findAllPublicByMembers_username(String username);

    @Query("SELECT ch FROM Chat ch JOIN ch.members m WHERE m.username = :username AND ch.name LIKE CONCAT(:prefix, '%') AND ch.isPrivate = true")
    List<Chat> findAllPrivateByPrefixAndMembers_username(String prefix, String username);

    @Query("SELECT ch from Chat ch JOIN ch.members m WHERE m.username = :username AND ch.isPrivate = true")
    public List<Chat> FindAllPrivateByMembers_username(String username);

    public Optional<Chat> findByIdToken(String idToken);
}
