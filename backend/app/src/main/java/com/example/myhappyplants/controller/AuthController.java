package com.example.myhappyplants.controller;

import com.example.myhappyplants.auth.JwtService;
import com.example.myhappyplants.dto.AuthResponse;
import com.example.myhappyplants.dto.LoginRequest;
import com.example.myhappyplants.dto.LogoutRequest;
import com.example.myhappyplants.dto.RegisterRequest;
import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register new user.
     * Recieves JSON-body (username/email/password) and returnerar JWT-token.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Login user
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

	@PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> logout(Authentication authentication) {
        authService.logout(authentication);
        return ResponseEntity.noContent().build();
        // return ResponseEntity.ok(authService.logout(request.getCredentials()));
	}
}
