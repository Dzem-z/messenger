package com.project.messenger.services;

import com.project.messenger.entities.User;
import com.project.messenger.entities.UserStatus;
import com.project.messenger.entities.VerificationToken;
import com.project.messenger.repositories.UserRepository;
import com.project.messenger.repositories.UserStatusRepository;
import com.project.messenger.repositories.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserStatusRepository userStatusRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public VerificationService(VerificationTokenRepository verificationTokenRepository, UserStatusRepository userStatusRepository, EmailService emailService, UserRepository userRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userStatusRepository = userStatusRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }


    public void startVerificationProcess(User user) {
        var userStatus = new UserStatus();
        userStatus.setUser(user);
        userStatus.setVerified(false);
        userStatusRepository.save(userStatus);

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

        var maybeUserStatus = userStatusRepository.findByUser(user);
        if (maybeUserStatus.isEmpty()) {
            return false;
        }

        UserStatus userStatus = maybeUserStatus.get();
        userStatus.setVerified(true);
        userStatusRepository.save(userStatus);

        verificationTokenRepository.delete(verificationToken);
        return true;
    }

    public boolean isUserVerified(String username) {
        var maybeUser = userRepository.findUserByUsername(username);
        if (maybeUser.isEmpty()) {
            return false;
        }

        var user = maybeUser.get();

        var maybeUserStatus = userStatusRepository.findByUser(user);
        if (maybeUserStatus.isEmpty()) {
            return false;
        }

        UserStatus userStatus = maybeUserStatus.get();

        return userStatus.isVerified();
    }
}
