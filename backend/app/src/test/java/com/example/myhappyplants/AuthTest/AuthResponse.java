package com.example.myhappyplants.AuthTest;

import lombok.Getter;

@Getter
class AuthResponse {
    private boolean success;
    private String email;
    private String sessionToken;
    private String errorMessage;

    private AuthResponse(boolean success, String email, String sessionToken, String errorMessage) {
        this.success = success;
        this.email = email;
        this.sessionToken = sessionToken;
        this.errorMessage = errorMessage;
    }

    public static AuthResponse success(String email, String token) {
        return new AuthResponse(true, email, token, null);
    }

    public static AuthResponse failure(String error) {
        return new AuthResponse(false, null, null, error);
    }

}