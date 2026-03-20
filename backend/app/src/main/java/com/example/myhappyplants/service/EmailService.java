package com.example.myhappyplants.service;

import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        // message.setFrom("myhappyplantsmau@gmail.com"); // only for sending with Gmail
        message.setSubject("Verify your MyHappyPlant account");
        message.setText("Click to verify your account:\n\n"
                + "http://localhost:5173/verify?token=" + token);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Could not send verification email, please try again later");
        }
    }
}
