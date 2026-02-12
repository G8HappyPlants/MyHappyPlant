package com.example.myhappyplants.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/api/me")
    public String me(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getUsername(); // email
    }
}