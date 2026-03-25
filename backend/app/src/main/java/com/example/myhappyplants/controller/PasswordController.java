package com.example.myhappyplants.controller;
import com.example.myhappyplants.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok(Map.of("message", "Återställningslänk har skickats till din e-post."));
        } catch (Exception e) {

            System.out.println("Error in forgotPassword: " + e.getMessage());
            return ResponseEntity.ok(Map.of("message", "Om e-postadressen finns i vårt system har en länk skickats."));
        }

    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        try {
            passwordService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Ditt lösenord har nu uppdaterats!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
        }
}
