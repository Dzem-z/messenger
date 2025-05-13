package com.project.messenger.services;

import com.project.messenger.entities.PasswordResetToken;
import com.project.messenger.repositories.PasswordResetTokenRepository;
import com.project.messenger.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService, UserRepository userRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public void startPasswordResetProcess(String email) {
        var maybeUser = userRepository.findUserByEmail(email);
        if (maybeUser.isEmpty())
            return;

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(maybeUser.get());
        passwordResetToken.setToken(UUID.randomUUID().toString());
        var created = LocalDateTime.now();
        var expired = created.plusMinutes(30);
        passwordResetToken.setCreatedAt(created);
        passwordResetToken.setExpiresAt(expired);
        passwordResetTokenRepository.save(passwordResetToken);

        emailService.sendPasswordResetMail(email, passwordResetToken.getToken());
    }

    public boolean verifyPasswordResetToken(String token) {
        var maybeToken = passwordResetTokenRepository.findByToken(token);
        if (maybeToken.isPresent()) {
            var passwordResetToken = maybeToken.get();
            var now = LocalDateTime.now();

            return now.isAfter(passwordResetToken.getCreatedAt())
                    && now.isBefore(passwordResetToken.getExpiresAt());
        }
        return false;
    }

    public void resetPassword(String token, String newPassword) {
        if (!verifyPasswordResetToken(token)) {
            return;
        }
        //I know that exist due to verify method
        var passwordResetToken = passwordResetTokenRepository.findByToken(token).get();
        passwordResetTokenRepository.delete(passwordResetToken);
        var user = passwordResetToken.getUser();
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}
