package com.project.messenger.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.messenger.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    
    public List<Message> findAllByChat_id(int id);
}
