package com.example.myhappyplants.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO för att registrera en ny användare.
 * Används som @RequestBody i POST /api/auth/register
 */

//Record möjliggör automatisk konstruktor, getters + equals, hashCode, toString
//Spring Boot (3+ / 4) + Jackson kan mappa JSON → record
// + köra @Valid på record-fields + @RequestBody
public record RegisterRequest(

        @NotBlank(message = "Username required")
        @Size(min = 3, max = 60, message = "Username must have a length between 3 and 60 characters")
        @Pattern(
                regexp = "^[a-zA-Z0-9_\\-]+$",
                message = "Username can only contain letters, numbers, underscores and hyphens")
        String username,

        @Pattern(
                regexp = ".*\\.[A-Za-z]{2,}$",
                message = "Email must include a top-level domain"
        )
        @Email(message = "Invalid email")
        @NotBlank(message = "e-mail required")
        @Size(max = 60)
        String email,

        @NotBlank(message = "Password required")
        @Size(min = 8, max = 60, message = "Password must be between 8 and 72 characters")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=(?:.*\\d){2,})(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,}$",
                message = "The password must contain at least one uppercase letter, one lowercase letter, two digits, and one special character."
        )
        String password
) {
}

