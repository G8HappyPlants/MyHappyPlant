package com.example.myhappyplants.dto;

/**
 * DTO som skickas tillbaka efter lyckad registrering.
 * Innehåller JWT-token.
 */
public record AuthResponse (String token) {}


