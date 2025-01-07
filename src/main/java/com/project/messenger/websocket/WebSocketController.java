package com.project.messenger.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.project.messenger.models.MessageDto;
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
        @DestinationVariable("chatId") String chatId) {
        log.info("Recieved message from user: " + "undefined" + ": " + message.getContent() + ", and chatId: " + chatId);
        
        messageService.saveMessage(chatId, message);

        messagingTemplate.convertAndSend("/topic/messages/" + chatId, message);

        log.info("Sent message to /topic/messages/" + chatId + ": " + message.getContent());
    }
    
}
