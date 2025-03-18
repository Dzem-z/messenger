package com.project.messenger.services;

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
import com.project.messenger.exceptions.InconsistentChatException;
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

    public List<Chat> findAllPublicChatsWithPrefixByUser(String prefix, User user) {
        return chatRepository.findAllPublicByPrefixAndMembers_username(prefix, user.getUsername());
    }

    public List<Chat> findAllPrivateChatsWithPrefixByUser(String prefix, User user) {
        return chatRepository.findAllPrivateByPrefixAndMembers_username(prefix, user.getUsername());
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

        if(chat.getIsPrivate() && chat.getMembers().size() != 2) //Check if private chat contains exactly two users.
            throw new InconsistentChatException("Cannot create private chat with " + chat.getMembers().size() + " users");

        Chat requestedChat = new Chat(
            0, 
            chat.getName(),
            idToken,
            members,
            new HashSet<>(),
            chat.getIsPrivate());

        System.out.println("chat: " + requestedChat);

        return chatRepository.save(requestedChat);
    }

    public Chat removeUserFromChat(User user, Chat chat) {
        chat.getMembers().remove(user);
        return chatRepository.save(chat); 
    }

    public void deleteChat(Chat chat) {
        chatRepository.delete(chat);
    }

    public Chat findChatbyId(int id) {
        return chatRepository.findById(id)
            .orElseThrow(() -> new ChatNotFoundException("No chat with id: " + id));
    }

}
