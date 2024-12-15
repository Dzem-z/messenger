package com.project.messenger.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.messenger.security.repositories.UserRepository;

@Configuration
public class DatabaseInit {
    
    private static final Logger log = LoggerFactory.getLogger(DatabaseInit.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            //userRepository.save(new User("user", "password"));
            
            userRepository.findAll().forEach(user -> log.info("Loaded " + user));
        };
    }

}
