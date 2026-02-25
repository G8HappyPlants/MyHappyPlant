package com.example.myhappyplants.service;

import com.example.myhappyplants.auth.JwtService;
import com.example.myhappyplants.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("ANV-02-F-04 Test AuthService.logout vidarebefordrar token till JwtService.destroyToken " )
    @Test
    void LOGOUT_authServiceLogout_callsJwtDestroyToken() {
        // TESTAR: att AuthService.logout(Authentication) tar token från Authentication.getCredentials()
        // och skickar den vidare till jwtService.destroyToken(token).

        String token = "valid.jwt.token";
        Authentication auth = mock(Authentication.class);
        when(auth.getCredentials()).thenReturn(token);

        // Förväntning: logout ska inte kasta exception (givet att JwtService-mocken inte kastar).
        assertDoesNotThrow(() -> authService.logout(auth));

        // Förväntning: AuthService ska anropa JwtService.destroyToken exakt med samma token.
        verify(jwtService).destroyToken(token);

        // Förväntning: AuthService ska inte göra andra anrop på JwtService i logout-flödet.
        verifyNoMoreInteractions(jwtService);
    }
}
