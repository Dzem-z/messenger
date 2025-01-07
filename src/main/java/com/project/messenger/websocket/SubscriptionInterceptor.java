package com.project.messenger.websocket;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.project.messenger.exceptions.SubscriptionDeniedException;
import com.project.messenger.repositories.UserRepository;

@Component
public class SubscriptionInterceptor implements ChannelInterceptor {
    
    UserRepository userRepository;

    public SubscriptionInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel)  {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            Authentication user = (Authentication) accessor.getUser();

            if (destination != null && destination.startsWith("/topic/messages")) {
                String destinationToken = destination.replaceFirst("^/topic/messages/", "");
                System.out.println("Token: " + destinationToken);
                List<String> allowedUsers = userRepository.findAllByChats_idToken(destinationToken).stream()
                    .map((u) -> u.getUsername())
                    .collect(Collectors.toList());

                if(user == null || !allowedUsers.stream().anyMatch(user.getName()::equals)) {
                    throw new SubscriptionDeniedException("user " + (user == null ? "undentified" : user.getName()) + " not allowed to subsribe to channel + " + channel);
                }
            }

        }

        return message;
    }
}
