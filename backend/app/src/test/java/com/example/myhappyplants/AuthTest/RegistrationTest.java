package com.example.myhappyplants.AuthTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationTest {

    @Mock
    private UserService userService;

    private static final String VALID_EMAIL = "VALID.USER@test.se";
    private static final String VALID_PASSWORD = "correctPassword123";
    private static final String EXISTING_EMAIL = "TAKEN.USER@test.se";
    private static final String INVALID_FORMAT_EMAIL = "INVALID.USER-no-at-sign";
    private static final String SHORT_PASSWORD = "123";
    private static final String SQL_INJECTION_EMAIL = "' OR 1=1 --";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("ANV-03-F1: Giltig registrering")
    void testRegistrationSuccess() {
        AuthResponse successResponse = AuthResponse.success(VALID_EMAIL, "fake-jwt-token-123");
        when(userService.register(VALID_EMAIL, VALID_PASSWORD)).thenReturn(successResponse);

        AuthResponse result = userService.register(VALID_EMAIL, VALID_PASSWORD);

        assertTrue(result.isSuccess());
        assertNull(result.getErrorMessage());
        assertEquals(VALID_EMAIL, result.getEmail());
    }

    @Test
    @DisplayName("ANV-03-F2: E-postadressen finns redan")
    void testRegistrationFailEmailExists() {
        AuthResponse failResponse = AuthResponse.failure("Email already exists");
        when(userService.register(EXISTING_EMAIL, VALID_PASSWORD)).thenReturn(failResponse);

        AuthResponse result = userService.register(EXISTING_EMAIL, VALID_PASSWORD);

        assertFalse(result.isSuccess());
        assertEquals("Email already exists", result.getErrorMessage());
    }

    @Test
    @DisplayName("ANV-03-F3: Felaktigt format på E-post")
    void testRegistrationFailInvalidEmailFormat() {
        AuthResponse failResponse = AuthResponse.failure("Invalid email format");
        when(userService.register(INVALID_FORMAT_EMAIL, VALID_PASSWORD)).thenReturn(failResponse);

        AuthResponse result = userService.register(INVALID_FORMAT_EMAIL, VALID_PASSWORD);

        assertFalse(result.isSuccess());
        assertEquals("Invalid email format", result.getErrorMessage());
    }

    @Test
    @DisplayName("ANV-03-F4: Lösenordet är för kort")
    void testRegistrationFailPasswordTooShort() {
        AuthResponse failResponse = AuthResponse.failure("Password too short");
        when(userService.register(VALID_EMAIL, SHORT_PASSWORD)).thenReturn(failResponse);

        AuthResponse result = userService.register(VALID_EMAIL, SHORT_PASSWORD);

        assertFalse(result.isSuccess());
        assertEquals("Password too short", result.getErrorMessage());
    }

    @Test
    @DisplayName("ANV-03-F6: Registrering med tom E-post")
    void testRegistrationFailEmptyEmail() {
        AuthResponse failResponse = AuthResponse.failure("Email cannot be empty");
        when(userService.register("", VALID_PASSWORD)).thenReturn(failResponse);

        AuthResponse result = userService.register("", VALID_PASSWORD);

        assertFalse(result.isSuccess());
        assertEquals("Email cannot be empty", result.getErrorMessage());
    }

    @Test
    @DisplayName("ANV-03-F7: Registrering med tomt lösenord")
    void testRegistrationFailEmptyPassword() {
        AuthResponse failResponse = AuthResponse.failure("Password cannot be empty");
        when(userService.register(VALID_EMAIL, "")).thenReturn(failResponse);

        AuthResponse result = userService.register(VALID_EMAIL, "");

        assertFalse(result.isSuccess());
        assertEquals("Password cannot be empty", result.getErrorMessage());
    }

    @Test
    @DisplayName("ANV-03-F8: Registrering med SQL Injection")
    void testRegistrationFailSqlInjection() {
        AuthResponse failResponse = AuthResponse.failure("Invalid input");
        when(userService.register(SQL_INJECTION_EMAIL, VALID_PASSWORD)).thenReturn(failResponse);

        AuthResponse result = userService.register(SQL_INJECTION_EMAIL, VALID_PASSWORD);

        assertFalse(result.isSuccess());
        assertNotNull(result.getErrorMessage());
    }
}