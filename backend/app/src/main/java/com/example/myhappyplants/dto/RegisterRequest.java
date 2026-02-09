package com.example.myhappyplants.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
        @Size(min = 3, message = "At least 3 characters required")
        String username,

        @Email(message = "Invalid email")
        @NotBlank(message = "e-mail required")
        String email,

        @NotBlank(message = "Password krävs")
        @Size(min = 8, message = "Password must contain at least 8 characters")
        String password
) {}

