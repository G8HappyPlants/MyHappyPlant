package com.example.myhappyplants.AuthTest;

import lombok.Getter;

@Getter
class AuthResponse {
    private String token;

    public AuthResponse(String token){
        this.token = token;
    }

}