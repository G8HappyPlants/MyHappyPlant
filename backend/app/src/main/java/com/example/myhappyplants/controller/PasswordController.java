package com.example.myhappyplants.controller;
import com.example.myhappyplants.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
@RestController
@RequestMapping("/api/password")
@CrossOrigin(origins = "http://localhost:5173")
public class PasswordController {
    @Autowired
    private PasswordService passwordService;

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        try {
            passwordService.createPasswordResetTokenForUser(email);
        } catch (ResponseStatusException e) {
            // Re-throw SMTP/service errors so they surface as 5xx
            throw e;
        } catch (Exception e) {
            // Swallow "user not found" to avoid leaking whether an email exists
            System.out.println("Error in forgotPassword: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of("message", "If that email exists in our system, a reset link has been sent."));

    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        try {
            passwordService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Your password has been updated!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
        }
}
