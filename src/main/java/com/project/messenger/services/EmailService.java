package com.project.messenger.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetMail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("messenger@hak.com");
        message.setTo(email);
        message.setSubject("Password Reset");
        message.setText("You can reset your password by clicking on the link below:\n"
        + "http://localhost:8081/reset-password/" + token);
        mailSender.send(message);
    }

    public void sendVerificationMail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("messenger@hak.com");
        message.setTo(email);
        message.setSubject("Verification");
        message.setText("You can verfy your email by clicking on the link below:\n"
        + "http://localhost:8081/verify/" + token);
        mailSender.send(message);
    }
}
