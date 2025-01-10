package com.project.messenger.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.messenger.entities.Chat;
import com.project.messenger.entities.User;
import com.project.messenger.exceptions.ChatNotFoundException;
import com.project.messenger.models.ChatDto;
import com.project.messenger.repositories.ChatRepository;
import com.project.messenger.repositories.UserRepository;

@Service
public class ChatService {
    
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public List<Chat> findAllChatsByUser(User user) {
        return chatRepository.findAllByMembers_username(user.getUsername());
    }

    public Chat createChat(ChatDto chat) {
        String idToken;
        do {
            idToken = UUID.randomUUID().toString();
        } while (chatRepository.findByIdToken(idToken).isPresent());
        System.out.println(idToken);

        Set<User> members = chat.getMembers().stream().map(user -> {
            return userRepository.findUserByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found " + user.getUsername()));
        }).collect(Collectors.toSet());

        Chat requestedChat = new Chat(
            0, 
            chat.getName(),
            idToken,
            members,
            new HashSet<>());

        System.out.println("chat: " + requestedChat);

        return chatRepository.save(requestedChat);
    }

    public Chat findChatbyId(int id) {
        return chatRepository.findById(id)
            .orElseThrow(() -> new ChatNotFoundException("No chat with id: " + id));
    }

}
