package com.example.myhappyplants.controller;

import com.example.myhappyplants.dto.AuthResponse;
import com.example.myhappyplants.dto.RegisterRequest;
import com.example.myhappyplants.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registrera ny användare.
     * Tar emot JSON-body (username/email/password) och returnerar JWT-token.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
