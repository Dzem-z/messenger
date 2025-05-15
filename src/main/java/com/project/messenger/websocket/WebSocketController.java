package com.project.messenger.websocket;


import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.project.messenger.entities.User;
import com.project.messenger.models.MessageDto;
import com.project.messenger.security.entities.SecurityUser;
import com.project.messenger.services.MessageService;
import com.project.messenger.services.UserService;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final UserService userService;
    
    public WebSocketController(SimpMessagingTemplate messagingTemplate, MessageService messageService, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.userService = userService;
    }

    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/chat/sendMessage/{chatId}")
    public void sendMessage(
        MessageDto message,
        @DestinationVariable("chatId") String chatId,
        Authentication authentication) throws UserPrincipalNotFoundException {
        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        User user = userService.findCurrentUser(principal);
        log.info("Recieved message from user: " + principal.getUsername() + ": " + message.getContent() + ", and chatId: " + chatId);


        MessageDto processedMessage = new MessageDto(messageService.saveMessage(chatId, message, user));

        messagingTemplate.convertAndSend("/topic/messages/" + chatId, processedMessage);

        log.info("Sent message to /topic/messages/" + chatId + ": " + message.getContent());
    }
    
}
