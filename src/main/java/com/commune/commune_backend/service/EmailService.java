package com.commune.commune_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String email, String code) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Commune Email Verification");
        message.setText(
                "Welcome to Commune!\n\n" +
                        "Your verification code is: " + code + "\n\n" +
                        "This code will expire in 10 minutes."
        );

        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String email, String code){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Commune Password Reset");
        message.setText(
                "You requested to reset your Commune password.\n\n" +
                        "Your password reset code is: " + code + "\n\n" +
                        "This code will expire in 10 minutes"
        );

        mailSender.send(message);
    }
}