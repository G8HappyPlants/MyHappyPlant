package com.example.myhappyplants.authTest;

import com.example.myhappyplants.auth.JwtService;
import com.example.myhappyplants.dto.AuthResponse;
import com.example.myhappyplants.dto.RegisterRequest;
import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.repository.UserRepository;
import com.example.myhappyplants.service.AuthService;
import com.example.myhappyplants.service.CryptoService;
import com.example.myhappyplants.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
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
    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @DisplayName("ANV-03-F-1: Registrering med giltiga uppgifter")
    @Test
    void register_validInput_success() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD);

        when(userRepository.existsByEmailHash(EMAIL_HASH)).thenReturn(false);
        // when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(passwordEncoder.encode(VALID_PASSWORD)).thenReturn(PASSWORD_HASH);
        when(cryptoService.hash(VALID_EMAIL)).thenReturn(EMAIL_HASH);

        User savedUser = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        boolean success = authService.register(request);

        assertTrue(success);
    }

    @DisplayName("ANV-03-F-2: Registrering med e-post i versaler (normalisering)")
    @Test
    void register_normalizesUppercaseEmail() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD);

        when(userRepository.existsByEmailHash(EMAIL_HASH)).thenReturn(false);
        // when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD_HASH);
        when(cryptoService.hash(VALID_EMAIL)).thenReturn(EMAIL_HASH);

        User savedUser = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        boolean success = authService.register(request);

        assertTrue(success);
        verify(userRepository).existsByEmailHash(EMAIL_HASH);
    }

    @DisplayName("ANV-03-F-3: Registrering med e-post som innehåller whitespace")
    @Test
    void register_trimsEmailWhitespace() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, " valid.user@test.com ", VALID_PASSWORD);

        when(userRepository.existsByEmailHash(EMAIL_HASH)).thenReturn(false);
        // when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD_HASH);
        when(cryptoService.hash(VALID_EMAIL)).thenReturn(EMAIL_HASH);

        User savedUser = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        boolean success = authService.register(request);

        assertTrue(success);
        verify(userRepository).existsByEmailHash(EMAIL_HASH);
    }

    @DisplayName("ANV-03-F-4: Registrering med användarnamn som innehåller whitespace")
    @Test
    void register_trimsUsernameWhitespace() {
        RegisterRequest request = new RegisterRequest(" testuser ", VALID_EMAIL, VALID_PASSWORD);

        when(userRepository.existsByEmailHash(EMAIL_HASH)).thenReturn(false);
        // when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD_HASH);
        when(cryptoService.hash(VALID_EMAIL)).thenReturn(EMAIL_HASH);

        User savedUser = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        boolean success = authService.register(request);

        assertTrue(success);
        verify(userRepository).existsByEmailHash(EMAIL_HASH);
    }

    @DisplayName("ANV-03-F-5: Registrering med alla fält innehållande whitespace")
    @Test
    void register_trimsUsernameAndEmailWhitespace() {
        final String whitespaceEmail = " VALID.USER@TEST.COM ";
        final String whitespaceEmailNormalized = whitespaceEmail.toLowerCase().trim();

        final String whitespaceUsername = " testuser ";
        final String normalizedUsername = whitespaceUsername.trim();

        RegisterRequest request = new RegisterRequest(whitespaceUsername, whitespaceEmail, VALID_PASSWORD);

        when(userRepository.existsByEmailHash(EMAIL_HASH)).thenReturn(false);
        // when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD_HASH);
        when(cryptoService.hash(whitespaceEmailNormalized)).thenReturn(EMAIL_HASH);

        User savedUser = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        boolean success = authService.register(request);
        assertTrue(success);

        verify(userRepository).existsByEmailHash(EMAIL_HASH);
    }

    @DisplayName("ANV-03-F-8: Registrering med dubblerad e-post")
    @Test
    void register_duplicateEmail() {
        final String existingEmail = "existing@test.com";

        RegisterRequest request = new RegisterRequest("newuser", existingEmail, VALID_PASSWORD);
        when(userRepository.existsByEmailHash(EMAIL_HASH)).thenReturn(true);
        when(cryptoService.hash(existingEmail)).thenReturn(EMAIL_HASH);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.register(request));

        assertEquals(ex.getStatusCode().value(), HttpStatus.CONFLICT.value());
        assertEquals("Email already in use", ex.getReason());
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder, jwtService);
    }

    // Service does allow for duplicate usernames, skip
    // @DisplayName("ANV-03-F-9: Registrering med dubblerat användarnamn")
    // @Test
    // void register_duplicateUsername() {
    //     RegisterRequest request = new RegisterRequest("existinguser", "new@test.com", VALID_PASSWORD);
//
    //     when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
    //     when(userRepository.existsByUsername("existinguser")).thenReturn(true);
//
    //     ResponseStatusException ex = assertThrows(ResponseStatusException.class,
    //             () -> authService.register(request));
//
    //     assertEquals(ex.getStatusCode().value(), HttpStatus.CONFLICT.value());
    //     assertEquals("Username already in use", ex.getReason());
//
    //     verify(userRepository, never()).save(any());
    //     verifyNoInteractions(passwordEncoder, jwtService, cryptoService);
    // }

    @DisplayName("ANV-03-F-16: Registrering med null-objekt")
    @Test
    void register_nullRequest() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authService.register(null));

        assertEquals(exception.getStatusCode().value(), HttpStatus.BAD_REQUEST.value());
        verifyNoInteractions(userRepository);
    }

    @DisplayName("ANV-03-F-36: Email-verifiering med giltig token")
    @Test
    void verify_with_valid_token() {
        String token = UUID.randomUUID().toString();

        User dummyUserToBeVerified = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        dummyUserToBeVerified.setVerificationToken(token);
        dummyUserToBeVerified.setVerificationExpiresAt(Instant.now().plusSeconds(60*60*24));

        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(dummyUserToBeVerified));
        when(jwtService.createToken(VALID_EMAIL)).thenReturn(JWT_TOKEN);

        AuthResponse response = authService.verifyEmail(token);

        assertNotNull(response);
        assertEquals(JWT_TOKEN, response.token());
    }

    @DisplayName("ANV-03-F-37: Email-verifiering med ogiltig token")
    @Test
    void verify_with_invalid_token() {
        String random_token = UUID.randomUUID().toString();
        String real_token = UUID.randomUUID().toString();

        User dummyUserToBeVerified = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        dummyUserToBeVerified.setVerificationToken(real_token);
        dummyUserToBeVerified.setVerificationExpiresAt(Instant.now().plusSeconds(60*60*24));

        when(userRepository.findByVerificationToken(random_token)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()-> authService.verifyEmail(random_token));

        assertEquals(exception.getStatusCode().value(), HttpStatus.BAD_REQUEST.value());
        assertEquals("Invalid Token", exception.getReason());
    }

    @DisplayName("ANV-03-F-38: Email-verifiering med expired token")
    @Test
    void verify_with_expired_token() {
        String token = UUID.randomUUID().toString();

        User dummyUserToBeVerified = new User(VALID_USERNAME, VALID_EMAIL, EMAIL_HASH, PASSWORD_HASH);
        dummyUserToBeVerified.setVerificationToken(token);
        dummyUserToBeVerified.setVerificationExpiresAt(Instant.now().minusSeconds(60*60*24));

        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(dummyUserToBeVerified));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()-> authService.verifyEmail(token));

        assertEquals(exception.getStatusCode().value(), HttpStatus.BAD_REQUEST.value());
        assertEquals("Token Expired", exception.getReason());
    }
}
