package com.project.messenger.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.messenger.entities.Chat;
import com.project.messenger.entities.Message;
import com.project.messenger.exceptions.ChatNotFoundException;
import com.project.messenger.exceptions.MessageNotFoundException;
import com.project.messenger.models.MessageDto;
import com.project.messenger.repositories.ChatRepository;
import com.project.messenger.repositories.MessageRepository;

@Service
public class MessageService {
    
    MessageRepository messageRepository;
    ChatRepository chatRepository;

    MessageService(MessageRepository messageRepository, ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    private Message saveMessage(Chat chat, MessageDto message) {
        int messageId = 0;
        OffsetDateTime dateOfPosting = OffsetDateTime.now();

        Message requestMessage = new Message(
            messageId,
            message.getContent(),
            dateOfPosting,
            chat
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

    public Message saveMessage(int id, MessageDto message) {
        Chat chat = chatRepository.findById(id)
            .orElseThrow(() -> new ChatNotFoundException("No chat with id: " + id));

        return saveMessage(chat, message);
    }

    public Message saveMessage(String idToken, MessageDto message) {
        Chat chat = chatRepository.findByIdToken(idToken)
            .orElseThrow(() -> new ChatNotFoundException("No chat with idToken: " + idToken));

        return saveMessage(chat, message);
    }
}
