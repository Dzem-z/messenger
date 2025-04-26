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
    private final ChatService chatService;
    private final UserService userService;

    MessageService(MessageRepository messageRepository, ChatService chatService, UserService userService) {
        this.messageRepository = messageRepository;
        this.chatService = chatService;
        this.userService = userService;
    }

    private Message saveMessage(Chat chat, MessageDto message, SecurityUser principal) throws UserPrincipalNotFoundException {
        int messageId = 0;
        OffsetDateTime dateOfPosting = OffsetDateTime.now();
        User currentUser = userService.findCurrentUser(principal);

        if(!chat.getMembers().contains(currentUser))
            throw new ChatNotFoundException("User " + currentUser.toString() + " is not a member of " + chat.toString() + ".");

        Message requestMessage = new Message(
            messageId,
            message.getContent(),
            dateOfPosting,
            chat,
            currentUser
        );

        return messageRepository.save(requestMessage);
    };

    public Message findById(int id) { //refactor. Take into account security context.
        return messageRepository.findById(id)
            .orElseThrow(() -> new MessageNotFoundException(id));
    }

    public List<Message> findAllByChatId(int id, SecurityUser member) throws UserPrincipalNotFoundException {
        chatService.findChatbyIdAndAuthorize(id, member);

        return messageRepository.findAllByChat_id(id); 
    }

    public Message saveMessage(int id, MessageDto message, SecurityUser author) throws UserPrincipalNotFoundException {
        Chat chat = chatService.findChatbyIdAndAuthorize(id, author);

        return saveMessage(chat, message, author);
    }

    public Message saveMessage(String idToken, MessageDto message, SecurityUser author) throws UserPrincipalNotFoundException {
        Chat chat = chatService.findChatbyIdTokenAndAuthorize(idToken, author);

        return saveMessage(chat, message, author);
    }
}
