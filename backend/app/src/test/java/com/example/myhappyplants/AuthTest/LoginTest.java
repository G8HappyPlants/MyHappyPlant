package com.example.myhappyplants.AuthTest;

import com.example.myhappyplants.auth.JwtService;
import com.example.myhappyplants.dto.AuthResponse;
import com.example.myhappyplants.dto.LoginRequest;
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

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private CryptoService cryptoService;

    @InjectMocks
    private AuthService authService; // ✅ riktig service, ingen @Mock

    private static final String VALID_EMAIL = "valid.user@test.com";
    private static final String VALID_PASSWORD = "correctPassword123!";
    private static final String WRONG_PASSWORD = "WrongPassword123!";
    private static final String NON_EXISTENT_EMAIL = "nonexistent.user@test.com";
    private static final String EMPTY_VALUE = "";
    private static final String WHITE_SPACE = "  ";
    private static final String UPPERCASE_EMAIL = "VALID.USER@TEST.COM";
    private static final String SQL_INJECTION = " ' OR 1=1-- ";
    private static final String EMAIL_WITH_WHITESPACE = " valid.user@test.com ";
    private static final String EMAIL_INVALID_FORMAT = "valid.usertest.com";

    // ===== helper to set a user exists for an email =====
    private void stubUserFoundForEmail(String normalizedEmail, String passwordHash) {
        String emailHash = "hash:" + normalizedEmail;

        when(cryptoService.hash(normalizedEmail)).thenReturn(emailHash);

        User user = mock(User.class);
        when(user.getEmail()).thenReturn(normalizedEmail);
        when(user.getPasswordHash()).thenReturn(passwordHash);

        when(userRepository.findByEmailHash(emailHash)).thenReturn(Optional.of(user));
    }

    private void stubUserNotFoundForEmail(String normalizedEmail) {
        String emailHash = "hash:" + normalizedEmail;
        when(cryptoService.hash(normalizedEmail)).thenReturn(emailHash);
        when(userRepository.findByEmailHash(emailHash)).thenReturn(Optional.empty());
    }

    // ================== POSITIVE TEST CASES ==================

    @DisplayName("test with valid email and password - ANV-01-F-1")
    @Test
    void testSuccessfulLoginWithValidCredentials() {
        // Arrange
        stubUserFoundForEmail(VALID_EMAIL, "pwHash");
        when(passwordEncoder.matches(VALID_PASSWORD, "pwHash")).thenReturn(true);
        when(jwtService.createToken(VALID_EMAIL)).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        verify(jwtService).createToken(VALID_EMAIL);
    }

    @DisplayName("test with valid uppercase email - ANV-01-F-2")
    @Test
    void testSuccessfulLoginWithUppercaseEmail() {
        String normalizedEmail = "valid.user@test.com";

        stubUserFoundForEmail(normalizedEmail, "pwHash");
        when(passwordEncoder.matches(VALID_PASSWORD, "pwHash")).thenReturn(true);
        when(jwtService.createToken(normalizedEmail)).thenReturn("jwt-token");

        AuthResponse response = authService.login(new LoginRequest(UPPERCASE_EMAIL, VALID_PASSWORD));

        assertEquals("jwt-token", response.token());
        verify(cryptoService).hash(normalizedEmail); // visar lowercase-normalisering
    }

    @DisplayName("test with email containing whitespace - ANV-01-F-3")
    @Test
    void testSuccessfulLoginWithEmailWhitespace() {
        String normalizedEmail = "valid.user@test.com";

        stubUserFoundForEmail(normalizedEmail, "pwHash");
        when(passwordEncoder.matches(VALID_PASSWORD, "pwHash")).thenReturn(true);
        when(jwtService.createToken(normalizedEmail)).thenReturn("jwt-token");

        AuthResponse response = authService.login(new LoginRequest(EMAIL_WITH_WHITESPACE, VALID_PASSWORD));

        assertEquals("jwt-token", response.token());
        verify(cryptoService).hash(normalizedEmail); // visar trim
    }

    @DisplayName("test with leading whitespace in email - ANV-01-F-4")
    @Test
    void testSuccessfulLoginWithLeadingWhitespaceEmail() {
        String normalizedEmail = "valid.user@test.com";

        stubUserFoundForEmail(normalizedEmail, "pwHash");
        when(passwordEncoder.matches(VALID_PASSWORD, "pwHash")).thenReturn(true);
        when(jwtService.createToken(normalizedEmail)).thenReturn("jwt-token");

        AuthResponse response = authService.login(new LoginRequest("  " + VALID_EMAIL, VALID_PASSWORD));

        assertEquals("jwt-token", response.token());
        verify(cryptoService).hash(normalizedEmail);
    }

    @DisplayName("test with trailing whitespace in email - ANV-01-F-5")
    @Test
    void testSuccessfulLoginWithTrailingWhitespaceEmail() {
        String normalizedEmail = "valid.user@test.com";

        stubUserFoundForEmail(normalizedEmail, "pwHash");
        when(passwordEncoder.matches(VALID_PASSWORD, "pwHash")).thenReturn(true);
        when(jwtService.createToken(normalizedEmail)).thenReturn("jwt-token");

        AuthResponse response = authService.login(new LoginRequest(VALID_EMAIL + "  ", VALID_PASSWORD));

        assertEquals("jwt-token", response.token());
        verify(cryptoService).hash(normalizedEmail);
    }

    // ================== NEGATIVE TEST CASES - WRONG CREDENTIALS ==================

    @DisplayName("test with wrong password - ANV-01-F-6")
    @Test
    void testFailedLoginWithWrongPassword() {
        stubUserFoundForEmail(VALID_EMAIL, "pwHash");
        when(passwordEncoder.matches(WRONG_PASSWORD, "pwHash")).thenReturn(false);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(VALID_EMAIL, WRONG_PASSWORD))
        );

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @DisplayName("test with non-existent email - ANV-01-F-7")
    @Test
    void testFailedLoginWithNonExistentEmail() {
        stubUserNotFoundForEmail(NON_EXISTENT_EMAIL);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(NON_EXISTENT_EMAIL, VALID_PASSWORD))
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @DisplayName("test with wrong password and wrong email - ANV-01-F-8")
    @Test
    void testFailedLoginWithWrongEmailAndPassword() {
        stubUserNotFoundForEmail(NON_EXISTENT_EMAIL);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(NON_EXISTENT_EMAIL, WRONG_PASSWORD))
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // ================== VALIDATION-LIKE CASES (obs: ej implementerat i AuthService) ==================
    // AuthService.login() validerar INTE e-postformat / tomma fält / SQL-injection.
    // Den trim+lowercase email och slår upp user. Det innebär att dessa fall blir NOT_FOUND eller UNAUTHORIZED,
    // inte "BadRequest", om ni inte har @Valid-annoteringar i controller/DTO.

    @DisplayName("test with email without @ symbol - ANV-01-F-9")
    @Test
    void testLoginWithInvalidEmailFormat() {
        // Ingen @Email-check i AuthService.login(); den kommer bara söka och få NOT_FOUND
        stubUserNotFoundForEmail(EMAIL_INVALID_FORMAT);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(EMAIL_INVALID_FORMAT, VALID_PASSWORD))
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @DisplayName("test with empty email - ANV-01-F-10")
    @Test
    void testLoginWithEmptyEmail() {
        // empty -> trim/lowercase -> "" -> NOT_FOUND
        stubUserNotFoundForEmail(EMPTY_VALUE);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(EMPTY_VALUE, VALID_PASSWORD))
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @DisplayName("test with empty password - ANV-01-F-11")
    @Test
    void testLoginWithEmptyPassword() {
        // user hittas, matches("", hash) -> false => UNAUTHORIZED
        stubUserFoundForEmail(VALID_EMAIL, "pwHash");
        when(passwordEncoder.matches(EMPTY_VALUE, "pwHash")).thenReturn(false);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(VALID_EMAIL, EMPTY_VALUE))
        );

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @DisplayName("test with both empty email and password - ANV-01-F-12")
    @Test
    void testLoginWithBothEmpty() {
        stubUserNotFoundForEmail(EMPTY_VALUE);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(EMPTY_VALUE, EMPTY_VALUE))
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @DisplayName("test with whitespace-only email - ANV-01-F-13")
    @Test
    void testLoginWithWhitespaceOnlyEmail() {
        // "  ".trim() -> "" -> NOT_FOUND
        stubUserNotFoundForEmail("");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(WHITE_SPACE, VALID_PASSWORD))
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @DisplayName("test with whitespace-only password - ANV-01-F-14")
    @Test
    void testLoginWithWhitespaceOnlyPassword() {
        stubUserFoundForEmail(VALID_EMAIL, "pwHash");
        when(passwordEncoder.matches(WHITE_SPACE, "pwHash")).thenReturn(false);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(VALID_EMAIL, WHITE_SPACE))
        );

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @DisplayName("test with null email - ANV-01-F-15")
    @Test
    void testLoginWithNullEmail() {
        // request.email().trim() -> NPE
        assertThrows(NullPointerException.class, () -> authService.login(new LoginRequest(null, VALID_PASSWORD)));
    }

    @DisplayName("test with null password - ANV-01-F-16")
    @Test
    void testLoginWithNullPassword() {
        stubUserFoundForEmail(VALID_EMAIL, "pwHash");
        // matches(null, ...) -> kan ge NPE beroende på encoder; vi förväntar generellt exception
        assertThrows(Exception.class, () -> authService.login(new LoginRequest(VALID_EMAIL, null)));
    }

    @DisplayName("test with both null email and password - ANV-01-F-17")
    @Test
    void testLoginWithBothNull() {
        assertThrows(NullPointerException.class, () -> authService.login(new LoginRequest(null, null)));
    }

    @DisplayName("test with null LoginRequest object - ANV-01-F-18")
    @Test
    void testLoginWithNullObject() {
        assertThrows(NullPointerException.class, () -> authService.login(null));
    }

    @DisplayName("test with SQL injection in email - ANV-01-F-19")
    @Test
    void testLoginWithSqlInjectionInEmail() {
        // Ingen SQL-injection-validering i AuthService.login(), så detta blir normalt NOT_FOUND
        String normalized = SQL_INJECTION.trim().toLowerCase();
        stubUserNotFoundForEmail(normalized);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(SQL_INJECTION, VALID_PASSWORD))
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @DisplayName("test with SQL injection in password - ANV-01-F-20")
    @Test
    void testLoginWithSqlInjectionInPassword() {
        stubUserFoundForEmail(VALID_EMAIL, "pwHash");
        when(passwordEncoder.matches(SQL_INJECTION, "pwHash")).thenReturn(false);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(VALID_EMAIL, SQL_INJECTION))
        );

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @DisplayName("test with SQL injection in both fields - ANV-01-F-21")
    @Test
    void testLoginWithSqlInjectionInBothFields() {
        String normalized = SQL_INJECTION.trim().toLowerCase();
        stubUserNotFoundForEmail(normalized);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest(SQL_INJECTION, SQL_INJECTION))
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }
}