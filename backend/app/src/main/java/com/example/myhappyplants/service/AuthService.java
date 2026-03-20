package com.example.myhappyplants.service;

import com.example.myhappyplants.auth.JwtService;
import com.example.myhappyplants.dto.AuthResponse;
import com.example.myhappyplants.dto.LoginRequest;
import com.example.myhappyplants.dto.RegisterRequest;
import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.time.Instant;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CryptoService cryptoService;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       CryptoService cryptoService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.cryptoService = cryptoService;
        this.emailService = emailService;
    }

    /**
     * Registrerar ny användare:
     * - normaliserar input (trim + lowercase email)
     * - kollar att username/email är lediga
     * - sparar user med hashat lösenord
     * - returnerar JWT (subject = email)
     */

    @Transactional
    public boolean register(RegisterRequest request) {
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase();
        String password = request.password();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use");
        }


        String hash = passwordEncoder.encode(password);
        User user = userRepository.save(
                new User(username, email, cryptoService.hash(email), hash)
        );

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationExpiresAt(Instant.now().plusSeconds(86400)); // 24 hours
        user.setEmailVerified(false);

        userRepository.save(user);

        emailService.sendVerificationEmail(email, verificationToken);

        return true;
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();
        String password = request.password();

        User user = userRepository.findByEmailHash(cryptoService.hash(email))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Login failed"));

        if (!user.isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Pleas verify your email first");
        }

        if (passwordEncoder.matches(password, user.getPasswordHash())) {

            String token = jwtService.createToken(user.getEmail());
            return new AuthResponse(token);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed");
    }

    public AuthResponse verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Token"));

        if (user.getVerificationExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token Expired");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpiresAt(null);
        userRepository.save(user);

        String jwtToken = jwtService.createToken(user.getEmail());

        return new AuthResponse(jwtToken); // logged in immediately

    }

    public void logout(Authentication request) {
        jwtService.destroyToken((String) request.getCredentials());
    }
}
