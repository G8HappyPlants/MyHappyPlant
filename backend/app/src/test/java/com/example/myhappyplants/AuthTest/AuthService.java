package com.example.myhappyplants.AuthTest;

import com.example.myhappyplants.dto.LoginRequest;
import com.example.myhappyplants.dto.LogoutRequest;
import com.example.myhappyplants.dto.RegisterRequest;

<<<<<<< HEAD
interface AuthService {
=======
interface AuthService<DeleteAccountRequest> {
>>>>>>> login-test
    AuthResponse login(LoginRequest loginRequest);
    void logout(LogoutRequest logoutRequest);
    AuthResponse register(RegisterRequest registerRequest);
}
