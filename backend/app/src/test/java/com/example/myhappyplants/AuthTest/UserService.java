package com.example.myhappyplants.AuthTest;

interface UserService {
    AuthResponse login(String email, String password);
    AuthResponse logout(String sessionToken);
}
