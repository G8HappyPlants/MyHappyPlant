package com.example.myhappyplants.authTest;

import com.example.myhappyplants.auth.JwtService;
import com.example.myhappyplants.dto.AuthResponse;
import com.example.myhappyplants.dto.RegisterRequest;
import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.repository.UserRepository;
import com.example.myhappyplants.service.AuthService;
import com.example.myhappyplants.service.CryptoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationTest {

    private static final String VALID_USERNAME = "testuser";
    private static final String VALID_EMAIL = "valid.user@test.com";
    private static final String VALID_PASSWORD = "ValidPass123!";

    private static final String EMPTY_VALUE = "";

    private static final String PASSWORD_HASH = "pwHash";
    private static final String EMAIL_HASH = "emailHash";
    private static final String JWT_TOKEN = "jwt-token";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private CryptoService cryptoService;

    @InjectMocks
    private AuthService authService;

    @DisplayName("ANV-03-F-1: Registrering med giltiga uppgifter")
    @Test
    void register_validInput_success() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD);

        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(passwordEncoder.encode(VALID_PASSWORD)).thenReturn(PASSWORD_HASH);
        when(cryptoService.hash(VALID_EMAIL)).thenReturn(EMAIL_HASH);

        User savedUser = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken(VALID_EMAIL)).thenReturn(JWT_TOKEN);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(JWT_TOKEN, response.token());
        verify(jwtService).createToken(VALID_EMAIL);
    }

    @DisplayName("ANV-03-F-2: Registrering med e-post i versaler (normalisering)")
    @Test
    void register_normalizesUppercaseEmail() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD);

        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD_HASH);
        when(cryptoService.hash(VALID_EMAIL)).thenReturn(EMAIL_HASH);

        User savedUser = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken(VALID_EMAIL)).thenReturn(JWT_TOKEN);

        AuthResponse response = authService.register(request);

        assertEquals(JWT_TOKEN, response.token());
        verify(userRepository).existsByEmail(VALID_EMAIL);
    }

    @DisplayName("ANV-03-F-3: Registrering med e-post som innehåller whitespace")
    @Test
    void register_trimsEmailWhitespace() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, " valid.user@test.com ", VALID_PASSWORD);

        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD_HASH);
        when(cryptoService.hash(VALID_EMAIL)).thenReturn(EMAIL_HASH);

        User savedUser = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken(VALID_EMAIL)).thenReturn(JWT_TOKEN);

        AuthResponse response = authService.register(request);

        assertEquals(JWT_TOKEN, response.token());
        verify(userRepository).existsByEmail(VALID_EMAIL);
    }

    @DisplayName("ANV-03-F-4: Registrering med användarnamn som innehåller whitespace")
    @Test
    void register_trimsUsernameWhitespace() {
        RegisterRequest request = new RegisterRequest(" testuser ", VALID_EMAIL, VALID_PASSWORD);

        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD_HASH);
        when(cryptoService.hash(VALID_EMAIL)).thenReturn(EMAIL_HASH);

        User savedUser = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken(VALID_EMAIL)).thenReturn(JWT_TOKEN);

        AuthResponse response = authService.register(request);

        assertEquals(JWT_TOKEN, response.token());
        verify(userRepository).existsByUsername(VALID_USERNAME);
    }

    @DisplayName("ANV-03-F-5: Registrering med alla fält innehållande whitespace")
    @Test
    void register_trimsUsernameAndEmailWhitespace() {
        RegisterRequest request = new RegisterRequest(" testuser ", " VALID.USER@TEST.COM ", VALID_PASSWORD);

        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD_HASH);
        when(cryptoService.hash(VALID_EMAIL)).thenReturn(EMAIL_HASH);

        User savedUser = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken(VALID_EMAIL)).thenReturn(JWT_TOKEN);

        AuthResponse response = authService.register(request);

        assertEquals(JWT_TOKEN, response.token());
        verify(userRepository).existsByEmail(VALID_EMAIL);
        verify(userRepository).existsByUsername(VALID_USERNAME);
    }

    @DisplayName("ANV-03-F-8: Registrering med dubblerad e-post")
    @Test
    void register_duplicateEmail() {
        RegisterRequest request = new RegisterRequest("newuser", "existing@test.com", VALID_PASSWORD);
        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.register(request));

        assertEquals("Email already in use", ex.getMessage());
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder, jwtService, cryptoService);
    }

    @DisplayName("ANV-03-F-9: Registrering med dubblerat användarnamn")
    @Test
    void register_duplicateUsername() {
        RegisterRequest request = new RegisterRequest("existinguser", "new@test.com", VALID_PASSWORD);

        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.register(request));

        assertEquals("Username already in use", ex.getMessage());
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder, jwtService, cryptoService);
    }

    @DisplayName("ANV-03-F-16: Registrering med null-objekt")
    @Test
    void register_nullRequest() {
        assertThrows(NullPointerException.class, () -> authService.register(null));
        verifyNoInteractions(userRepository);
    }
}