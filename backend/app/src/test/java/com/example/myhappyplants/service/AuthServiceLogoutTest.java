package com.example.myhappyplants.service;

import com.example.myhappyplants.auth.JwtService;
import com.example.myhappyplants.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceLogoutTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;
    @Mock CryptoService cryptoService;

    @InjectMocks AuthService authService;


    @Test
    void UT_LOGOUT_1_authServiceLogout_callsJwtDestroyToken() {
        String token = "valid.jwt.token";
        Authentication auth = mock(Authentication.class);
        when(auth.getCredentials()).thenReturn(token);

        assertDoesNotThrow(() -> authService.logout(auth));

        verify(jwtService).destroyToken(token);
        verifyNoMoreInteractions(jwtService);
    }
}
