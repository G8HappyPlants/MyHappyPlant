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

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private CryptoService cryptoService;

    @InjectMocks
    private AuthService authService;

    @DisplayName("ANV-03-F-1: Registrering med giltiga uppgifter")
    @Test
    void register_validInput_success() {
        RegisterRequest request = new RegisterRequest("testuser", "valid.user@test.com", "ValidPass123!");

        when(userRepository.existsByEmail("valid.user@test.com")).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);

        when(passwordEncoder.encode("ValidPass123!")).thenReturn("pwHash");
        when(cryptoService.hash("valid.user@test.com")).thenReturn("emailHash");

        User savedUser = new User("testuser", "valid.user@test.com", "emailHash", "pwHash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        when(jwtService.createToken("valid.user@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        verify(jwtService).createToken("valid.user@test.com");
    }

    @DisplayName("ANV-03-F-2: Registrering med e-post i versaler (normalisering)")
    @Test
    void register_normalizesUppercaseEmail() {
        RegisterRequest request = new RegisterRequest("testuser", "VALID.USER@TEST.COM", "ValidPass123!");
        String normalizedEmail = "valid.user@test.com";

        when(userRepository.existsByEmail(normalizedEmail)).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("pwHash");
        when(cryptoService.hash(normalizedEmail)).thenReturn("emailHash");

        User savedUser = new User("testuser", normalizedEmail, "emailHash", "pwHash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken(normalizedEmail)).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertEquals("jwt-token", response.token());
        verify(userRepository).existsByEmail(normalizedEmail);
    }

    @DisplayName("ANV-03-F-3: Registrering med e-post som innehåller whitespace")
    @Test
    void register_trimsEmailWhitespace() {
        RegisterRequest request = new RegisterRequest("testuser", " valid.user@test.com ", "ValidPass123!");
        String normalizedEmail = "valid.user@test.com";

        when(userRepository.existsByEmail(normalizedEmail)).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("pwHash");
        when(cryptoService.hash(normalizedEmail)).thenReturn("emailHash");

        User savedUser = new User("testuser", normalizedEmail, "emailHash", "pwHash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken(normalizedEmail)).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertEquals("jwt-token", response.token());
        verify(userRepository).existsByEmail(normalizedEmail);
    }

    @DisplayName("ANV-03-F-4: Registrering med användarnamn som innehåller whitespace")
    @Test
    void register_trimsUsernameWhitespace() {
        RegisterRequest request = new RegisterRequest(" testuser ", "valid.user@test.com", "ValidPass123!");
        String normalizedUsername = "testuser";

        when(userRepository.existsByEmail("valid.user@test.com")).thenReturn(false);
        when(userRepository.existsByUsername(normalizedUsername)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("pwHash");
        when(cryptoService.hash("valid.user@test.com")).thenReturn("emailHash");

        User savedUser = new User(normalizedUsername, "valid.user@test.com", "emailHash", "pwHash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken("valid.user@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertEquals("jwt-token", response.token());
        verify(userRepository).existsByUsername(normalizedUsername);
    }

    @DisplayName("ANV-03-F-5: Registrering med alla fält innehållande whitespace")
    @Test
    void register_trimsUsernameAndEmailWhitespace() {
        RegisterRequest request = new RegisterRequest(" testuser ", " VALID.USER@TEST.COM ", "ValidPass123!");
        String normalizedUsername = "testuser";
        String normalizedEmail = "valid.user@test.com";

        when(userRepository.existsByEmail(normalizedEmail)).thenReturn(false);
        when(userRepository.existsByUsername(normalizedUsername)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("pwHash");
        when(cryptoService.hash(normalizedEmail)).thenReturn("emailHash");

        User savedUser = new User(normalizedUsername, normalizedEmail, "emailHash", "pwHash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken(normalizedEmail)).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertEquals("jwt-token", response.token());
        verify(userRepository).existsByEmail(normalizedEmail);
        verify(userRepository).existsByUsername(normalizedUsername);
    }

    @DisplayName("ANV-03-F-6: Registrering med minimalt giltigt lösenord")
    @Test
    void register_minimalValidPassword_success() {
        String minimalPassword = "Aa12!";
        String normalizedUsername = "testuser";
        String normalizedEmail = "valid.user@test.com";
        RegisterRequest request = new RegisterRequest(normalizedUsername, normalizedEmail, minimalPassword);

        when(userRepository.existsByEmail(normalizedEmail)).thenReturn(false);
        when(userRepository.existsByUsername(normalizedUsername)).thenReturn(false);
        when(passwordEncoder.encode(minimalPassword)).thenReturn("pwHash");
        when(cryptoService.hash(normalizedEmail)).thenReturn("emailHash");

        User savedUser = new User(normalizedUsername, normalizedEmail, "emailHash", "pwHash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken(normalizedEmail)).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        verify(passwordEncoder).encode(minimalPassword);
        verify(userRepository).existsByUsername(normalizedUsername);
    }

    @DisplayName("ANV-03-F-7: Registrering med minimalt giltigt användarnamn")
    @Test
    void register_minimalValidUsername_success() {
        String normalizedPassword = "ValidPass123!";
        String minimalUsername = "tes";
        String normalizedEmail = "valid.user@test.com";
        RegisterRequest request = new RegisterRequest(minimalUsername, normalizedEmail, normalizedPassword);

        when(userRepository.existsByEmail(normalizedEmail)).thenReturn(false);
        when(userRepository.existsByUsername(minimalUsername)).thenReturn(false);
        when(passwordEncoder.encode(normalizedPassword)).thenReturn("pwHash");
        when(cryptoService.hash(normalizedEmail)).thenReturn("emailHash");

        User savedUser = new User(minimalUsername, normalizedEmail, "emailHash", "pwHash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken(normalizedEmail)).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        verify(userRepository).existsByUsername(minimalUsername);
        verify(jwtService).createToken(normalizedEmail);
    }

    @DisplayName("ANV-03-F-8: Registrering med dubblerad e-post")
    @Test
    void register_duplicateEmail() {
        RegisterRequest request = new RegisterRequest("newuser", "existing@test.com", "ValidPass123!");
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
        RegisterRequest request = new RegisterRequest("existinguser", "new@test.com", "ValidPass123!");

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