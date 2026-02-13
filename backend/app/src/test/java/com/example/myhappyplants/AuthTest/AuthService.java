package com.example.myhappyplants.AuthTest;

interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    void logout(LogoutRequest logoutRequest);
    void deleteAccount(DeleteAccountRequest deleteAccountRequest);
    AuthResponse register(RegisterRequest registerRequest);
}
