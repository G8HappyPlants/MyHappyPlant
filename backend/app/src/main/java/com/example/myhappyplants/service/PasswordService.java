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
                    .orElseThrow(()-> new RuntimeException("Kunde inte hitta en användare med den e-postadressen"));

            tokenRepository.deleteByUser(user);

            String token = UUID.randomUUID().toString();

            PasswordResetToken myToken = new PasswordResetToken(token, user);
            tokenRepository.save(myToken);

            String resetUrl = "http://localhost:5173/reset-password?token=" + token;

            String subject = "Återställ lösenord - My Happy Plants";
            String body = "Hej! Klicka på länken för att välja ett nytt lösenord: " + resetUrl;

            emailService.sendPasswordResetEmail(email, token);

            System.out.println("DEBUG: Reset link sent to " + email);

        }

        @Transactional
        public void resetPassword(String token, String newPassword) {
            PasswordResetToken resetToken = tokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Länken är ogiltig eller har redan använts."));

            if (resetToken.isExpired()) {
                tokenRepository.delete(resetToken);
                throw new RuntimeException("Länken har gått ut. Be om en ny.");
            }

            User user = resetToken.getUser();
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPasswordHash(encryptedPassword);

            userRepository.save(user);
            tokenRepository.delete(resetToken);
            System.out.println("DEBUG: Password updated for user: " + user.getUsername());
        }

    }



