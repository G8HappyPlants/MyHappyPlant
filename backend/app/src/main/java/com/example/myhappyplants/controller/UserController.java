package com.example.myhappyplants.controller;

import com.example.myhappyplants.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public String retrieveMe(@AuthenticationPrincipal UserDetails selfUser) {
        return selfUser.getUsername(); // email
    }

    /**
     * Deletes the current authenticated user
     *
     * @return {@code HTTP 204} on successful delete
     */
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal UserDetails selfUser) {
        userService.removeAccountByUserDetails(selfUser);
        return ResponseEntity.noContent().build();
    }
}