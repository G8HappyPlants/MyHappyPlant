package com.example.myhappyplants.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "You must enter password")
        String password
) {
}