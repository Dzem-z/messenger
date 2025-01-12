package com.project.messenger.websocket;


import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.project.messenger.models.MessageDto;
import com.project.messenger.security.entities.SecurityUser;
import com.project.messenger.services.MessageService;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    
    public WebSocketController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/chat/sendMessage/{chatId}")
    public void sendMessage(
        MessageDto message,
        @DestinationVariable("chatId") String chatId,
        Authentication authentication) throws UserPrincipalNotFoundException {
        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        log.info("Recieved message from user: " + principal.getUsername() + ": " + message.getContent() + ", and chatId: " + chatId);

        MessageDto processedMessage = new MessageDto(messageService.saveMessage(chatId, message, principal));

        messagingTemplate.convertAndSend("/topic/messages/" + chatId, processedMessage);

        log.info("Sent message to /topic/messages/" + chatId + ": " + message.getContent());
    }
    
}
