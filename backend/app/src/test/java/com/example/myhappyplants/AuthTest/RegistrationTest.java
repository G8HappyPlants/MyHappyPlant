package com.example.myhappyplants.AuthTest;

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

    @DisplayName("ANV-02-F-1: Valid registration saves user and returns JWT")
    @Test
    void register_validInput_success() {

        RegisterRequest request =
                new RegisterRequest("testuser", "valid.user@test.com", "ValidPass123!");

        when(userRepository.existsByEmail("valid.user@test.com")).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);

        when(passwordEncoder.encode("ValidPass123!")).thenReturn("pwHash");
        when(cryptoService.hash("valid.user@test.com")).thenReturn("emailHash");

        User savedUser = new User(
                "testuser",
                "valid.user@test.com",
                "emailHash",
                "pwHash"
        );

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.createToken("valid.user@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());

        // Verifiera att save anropas
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User createdUser = captor.getValue();
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("valid.user@test.com", createdUser.getEmail());
        assertEquals("pwHash", createdUser.getPasswordHash());

        verify(jwtService).createToken("valid.user@test.com");
    }

    @DisplayName("ANV-02-F-2: Uppercase email is normalized to lowercase")
    @Test
    void register_normalizesUppercaseEmail() {

        RegisterRequest request =
                new RegisterRequest("testuser", "VALID.USER@TEST.COM", "ValidPass123!");

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

    @DisplayName("ANV-02-F-3: Email with whitespace is trimmed")
    @Test
    void register_trimsEmailWhitespace() {

        RegisterRequest request =
                new RegisterRequest("testuser", " valid.user@test.com ", "ValidPass123!");

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

    @DisplayName("ANV-02-F-4: Username with whitespace is trimmed")
    @Test
    void register_trimsUsernameWhitespace() {

        RegisterRequest request =
                new RegisterRequest(" testuser ", "valid.user@test.com", "ValidPass123!");

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

    @DisplayName("ANV-02-F-5: Username and email with whitespace are normalized")
    @Test
    void register_trimsUsernameAndEmailWhitespace() {

        RegisterRequest request =
                new RegisterRequest(" testuser ", " VALID.USER@TEST.COM ", "ValidPass123!");

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

    @DisplayName("ANV-02-F-8: Duplicate email throws exception")
    @Test
    void register_duplicateEmail() {

        RegisterRequest request =
                new RegisterRequest("newuser", "existing@test.com", "ValidPass123!");

        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authService.register(request));

        assertEquals("Email already in use", ex.getMessage());

        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder, jwtService, cryptoService);
    }

    @DisplayName("ANV-02-F-9: Duplicate username throws exception")
    @Test
    void register_duplicateUsername() {

        RegisterRequest request =
                new RegisterRequest("existinguser", "new@test.com", "ValidPass123!");

        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authService.register(request));

        assertEquals("Username already in use", ex.getMessage());

        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder, jwtService, cryptoService);
    }

    @DisplayName("ANV-02-F-16: Null request throws NullPointerException")
    @Test
    void register_nullRequest() {
        assertThrows(NullPointerException.class,
                () -> authService.register(null));

        verifyNoInteractions(userRepository);
    }
}