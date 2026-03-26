package com.example.myhappyplants.service;
import com.example.myhappyplants.entity.PasswordResetToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.repository.PasswordResetTokenRepository;
import com.example.myhappyplants.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

    @Service
    public class PasswordService {

        @Autowired
        private PasswordResetTokenRepository tokenRepository;

        @Autowired
        private CryptoService cryptoService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private EmailService emailService;

        @Transactional
        public void createPasswordResetTokenForUser(String email){

            String emailHash = cryptoService.hash(email.trim().toLowerCase());

            User user = userRepository.findByEmailHash(emailHash)
                    .orElseThrow(()-> new RuntimeException("No account found with that email address."));

            tokenRepository.deleteByUser(user);

            String token = UUID.randomUUID().toString();

            PasswordResetToken myToken = new PasswordResetToken(token, user);
            tokenRepository.save(myToken);

            String resetUrl = "http://localhost:5173/reset-password?token=" + token;

            String subject = "Reset your password - My Happy Plants";
            String body = "Click the link below to choose a new password: " + resetUrl;

            emailService.sendPasswordResetEmail(email, token);

            System.out.println("DEBUG: Reset link sent to " + email);

        }

        @Transactional
        public void resetPassword(String token, String newPassword) {
            PasswordResetToken resetToken = tokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("This link is invalid or has already been used."));

            if (resetToken.isExpired()) {
                tokenRepository.delete(resetToken);
                throw new RuntimeException("This link has expired. Please request a new one.");
            }

            User user = resetToken.getUser();
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPasswordHash(encryptedPassword);

            userRepository.save(user);
            tokenRepository.delete(resetToken);
            System.out.println("DEBUG: Password updated for user: " + user.getUsername());
        }

    }



