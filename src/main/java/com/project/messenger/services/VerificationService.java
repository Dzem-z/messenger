package com.project.messenger.services;

import com.project.messenger.entities.User;
import com.project.messenger.entities.VerificationToken;
import com.project.messenger.repositories.UserRepository;
import com.project.messenger.repositories.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public VerificationService(VerificationTokenRepository verificationTokenRepository, UserRepository userRepository, EmailService emailService) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public void sendVerificationMail(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        var now = LocalDateTime.now();
        verificationToken.setCreatedAt(now);
        verificationToken.setExpiresAt(now.plusHours(24));
        verificationTokenRepository.save(verificationToken);

        emailService.sendVerificationMail(user.getEmail(), token);
    }

    public boolean verifyUser(String token) {
        var maybeVerificationToken = verificationTokenRepository.findByToken(token);
        if (maybeVerificationToken.isEmpty()) {
            return false;
        }

        var verificationToken = maybeVerificationToken.get();
        if (LocalDateTime.now().isBefore(verificationToken.getCreatedAt())
                || LocalDateTime.now().isAfter(verificationToken.getExpiresAt())) {
            return false;
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return true;
    }
}
