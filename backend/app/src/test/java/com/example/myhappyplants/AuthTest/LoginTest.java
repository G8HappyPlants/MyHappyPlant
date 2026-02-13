package com.example.myhappyplants.AuthTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginTest {

    @Mock
    private AuthService authService;

    private static final String USERNAME = "testuser";
    private static final String VALID_EMAIL = "valid.user@test.com";
    private static final String VALID_PASSWORD = "correctPassword123!";
    private static final String WRONG_PASSWORD = "wrongpassword";
    private static final String NON_EXISTENT_EMAIL = "nonexistent.user@test.com";
    private static final String EMPTY_VALUE = "";
    private static final String UPPERCASE_VALID_EMAIL = "VALID.USER@TEST.COM";
    private static final String SQL_INJECTION_1 = " ' OR 1=1-- ";
    private static final String SQL_INJECTION_2 = " ' UNION SELECT * FROM users-- ";

    @BeforeEach
    void setUp() {
        authService.register(new RegisterRequest(USERNAME, VALID_EMAIL, VALID_PASSWORD));
    }

    @DisplayName("test with valid email and password")
    @Test
    void testSuccessfulLoginWithValidCredentials() {

        LoginRequest request = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @DisplayName("test with invalid password")
    @Test
    void testFailedLoginWithWrongPassword() {

        LoginRequest request = new LoginRequest(VALID_EMAIL, WRONG_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with not existing email")
    @Test
    void testFailedLoginWithNoneExistedEmail() {
        LoginRequest request = new LoginRequest(NON_EXISTENT_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with empty email value")
    @Test
    void testFailedLoginWithEmptyEmail() {
        LoginRequest request = new LoginRequest(EMPTY_VALUE, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with empty password value")
    @Test
    void testFailedLoginWithEmptyPassword() {
        LoginRequest request = new LoginRequest(VALID_EMAIL, EMPTY_VALUE);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with null email")
    @Test
    void testFailedLoginWithNullEmail() {
        LoginRequest request = new LoginRequest(null, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with null password")
    @Test
    void testFailedLoginWithNullPassword() {
        LoginRequest request = new LoginRequest(VALID_EMAIL, null);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with sql-injection")
    @Test
    void testFailedLogInWithSqlInjection() {
        LoginRequest request = new LoginRequest(SQL_INJECTION_1, EMPTY_VALUE);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with sql-injection")
    @Test
    void testFailedLogInWithSqlInjectionUnion() {
        LoginRequest request = new LoginRequest(SQL_INJECTION_2, EMPTY_VALUE);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with valid uppercase email and password")
    @Test
    void testSuccessfulLoginWithUpperCaseEmail() {
        LoginRequest request = new LoginRequest(UPPERCASE_VALID_EMAIL, VALID_PASSWORD);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }
}




