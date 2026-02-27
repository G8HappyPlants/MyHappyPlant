package com.example.myhappyplants.AuthTest;

import com.example.myhappyplants.dto.LoginRequest;
import com.example.myhappyplants.dto.LogoutRequest;
import com.example.myhappyplants.dto.RegisterRequest;

interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    void logout(LogoutRequest logoutRequest);
    AuthResponse register(RegisterRequest registerRequest);
}
