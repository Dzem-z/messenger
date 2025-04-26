package com.project.messenger.services;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
import com.project.messenger.security.entities.SecurityUser;

@Service
public class ChatService {
    
    private final ChatRepository chatRepository;
    private final UserService userService;

    public ChatService(ChatRepository chatRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    public List<Chat> findAllPublicChatsWithPrefixByUser(String prefix, User user) {
        /*
         * Returns all public chats that match prefix and which the user is a member.
         */
        return chatRepository.findAllPublicByPrefixAndMembers_username(prefix, user.getUsername());
    }

    public List<Chat> findAllPrivateChatsWithPrefixByUser(String prefix, User user) {
        /*
         * Returns all private chats that match prefix and which the user is a member.
         */
        return chatRepository.findAllPrivateByPrefixAndMembers_username(prefix, user.getUsername());
    }

    public Chat createChat(ChatDto chat) {
        /*
         * Creates a chat.
         */
        String idToken;
        do {
            idToken = UUID.randomUUID().toString();
        } while (chatRepository.findByIdToken(idToken).isPresent());
        System.out.println(idToken);

        Set<User> members = chat.getMembers().stream().map(user -> {
            return userService.findUserByUsername(user.getUsername()); //refactor
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

    public Optional<Chat> removeUserFromChat(User user, Chat chat) {
        /*
         * Removes the user from the chat and if the user count doesn't satisfy predicate deletes the chat.
         */
        chat.getMembers().remove(user);

        if(chat.getMembers().isEmpty() || (chat.getIsPrivate() && chat.getMembers().size() != 2)) {
            deleteChat(chat);
            return Optional.empty();
        } else
            return Optional.of(chatRepository.save(chat)); 
    }

    public void deleteChat(Chat chat) {
        /*
         * Deletes the chat.
         */
        chatRepository.delete(chat);
    }

    public Chat findChatbyIdAndAuthorize(int id, SecurityUser principal) throws UserPrincipalNotFoundException {
        /*
         * Finds chat by the id if user is member of the chat.
         * Otherwise throws ChatNotFoundException.
         */
        Chat chat = chatRepository.findById(id)
            .orElseThrow(() -> new ChatNotFoundException("No chat with id: " + id));

        User user = userService.findCurrentUser(principal);

        if(!chat.getMembers().contains(user))
            throw new ChatNotFoundException("User not authorized to view chat " + chat.toString() + ".");
        
        return chat;
    }

    public Chat findChatbyIdTokenAndAuthorize(String idToken, SecurityUser principal) throws UserPrincipalNotFoundException {
        /*
         * Finds chat by the idToken if user is member of the chat.
         * Otherwise throws ChatNotFoundException.
         */
        Chat chat = chatRepository.findByIdToken(idToken)
            .orElseThrow(() -> new ChatNotFoundException("No chat with idToken: " + idToken));

            User user = userService.findCurrentUser(principal);

        if(!chat.getMembers().contains(user))
            throw new ChatNotFoundException("User not authorized to view chat " + chat.toString() + ".");
            
        return chat;
    }

}
