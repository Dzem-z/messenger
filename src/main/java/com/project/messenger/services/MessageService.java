package com.project.messenger.services;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.stereotype.Service;

import com.project.messenger.entities.Chat;
import com.project.messenger.entities.Message;
import com.project.messenger.entities.User;
import com.project.messenger.exceptions.ChatNotFoundException;
import com.project.messenger.exceptions.MessageNotFoundException;
import com.project.messenger.models.MessageDto;
import com.project.messenger.repositories.ChatRepository;
import com.project.messenger.repositories.MessageRepository;
import com.project.messenger.repositories.UserRepository;
import com.project.messenger.security.entities.SecurityUser;

@Service
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserService userService;

    MessageService(MessageRepository messageRepository, ChatRepository chatRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    private Message saveMessage(Chat chat, MessageDto message, SecurityUser principal) throws UserPrincipalNotFoundException {
        int messageId = 0;
        OffsetDateTime dateOfPosting = OffsetDateTime.now();
        User currentUser = userService.findCurrentUser(principal);

        Message requestMessage = new Message(
            messageId,
            message.getContent(),
            dateOfPosting,
            chat,
            currentUser
        );

        return messageRepository.save(requestMessage);
    };

    public Message findById(int id) {
        return messageRepository.findById(id)
            .orElseThrow(() -> new MessageNotFoundException(id));
    }

    public List<Message> findAllByChat_id(int id) {
        return messageRepository.findAllByChat_id(id);
    }

    public Message saveMessage(int id, MessageDto message, SecurityUser author) throws UserPrincipalNotFoundException {
        Chat chat = chatRepository.findById(id)
            .orElseThrow(() -> new ChatNotFoundException("No chat with id: " + id));

        return saveMessage(chat, message, author);
    }

    public Message saveMessage(String idToken, MessageDto message, SecurityUser author) throws UserPrincipalNotFoundException {
        Chat chat = chatRepository.findByIdToken(idToken)
            .orElseThrow(() -> new ChatNotFoundException("No chat with idToken: " + idToken));

        return saveMessage(chat, message, author);
    }
}
