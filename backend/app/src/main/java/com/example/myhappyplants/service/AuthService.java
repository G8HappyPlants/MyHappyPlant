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

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CryptoService cryptoService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, CryptoService cryptoService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.cryptoService = cryptoService;
    }

    /**
     * Registrerar ny användare:
     * - normaliserar input (trim + lowercase email)
     * - kollar att username/email är lediga
     * - sparar user med hashat lösenord
     * - returnerar JWT (subject = email)
     */

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase();
        String password = request.password();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already in use");
        }


        String hash = passwordEncoder.encode(password);
        User saved = userRepository.save(
                new User(username, email, cryptoService.hash(email), hash)
        );

        //JWT subject=email, set up för annat senare
        String token = jwtService.createToken(saved.getEmail());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();
        String password = request.password();

        User user = userRepository.findByEmailHash(cryptoService.hash(email))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User entry not found"));

        if (passwordEncoder.matches(password, user.getPasswordHash())) {

            String token = jwtService.createToken(user.getEmail());
            return new AuthResponse(token);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    public void logout(Authentication request) {
        jwtService.destroyToken((String) request.getCredentials());
    }
}
