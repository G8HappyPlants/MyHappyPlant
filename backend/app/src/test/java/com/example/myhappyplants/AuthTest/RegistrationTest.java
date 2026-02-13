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
    private AuthService authService;

    private static final String USERNAME = "testuser";
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
        RegisterRequest request = new RegisterRequest(USERNAME, VALID_EMAIL, VALID_PASSWORD);

        AuthResponse result = authService.register(request);

        assertNotNull(result);
        assertNotNull(result.getToken());
    }

    @Test
    @DisplayName("ANV-03-F2: E-postadressen finns redan")
    void testRegistrationFailEmailExists() {
        authService.register(new RegisterRequest("user1", EXISTING_EMAIL, VALID_PASSWORD));

        RegisterRequest duplicateRequest = new RegisterRequest("user2", EXISTING_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(duplicateRequest));
    }

    @Test
    @DisplayName("ANV-03-F3: Felaktigt format på E-post")
    void testRegistrationFailInvalidEmailFormat() {
        RegisterRequest request = new RegisterRequest(USERNAME, INVALID_FORMAT_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @Test
    @DisplayName("ANV-03-F4: Lösenordet är för kort")
    void testRegistrationFailPasswordTooShort() {
        RegisterRequest request = new RegisterRequest(USERNAME, VALID_EMAIL, SHORT_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @Test
    @DisplayName("ANV-03-F6: Registrering med tom E-post")
    void testRegistrationFailEmptyEmail() {
        RegisterRequest request = new RegisterRequest(USERNAME, "", VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @Test
    @DisplayName("ANV-03-F7: Registrering med tomt lösenord")
    void testRegistrationFailEmptyPassword() {
        RegisterRequest request = new RegisterRequest(USERNAME, VALID_EMAIL, "");

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @Test
    @DisplayName("ANV-03-F8: Registrering med SQL Injection")
    void testRegistrationFailSqlInjection() {
        RegisterRequest request = new RegisterRequest(USERNAME, SQL_INJECTION_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }
}