package com.example.myhappyplants.AuthTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginTest {

    @Mock
    private UserService userService;

    private static final String VALID_EMAIL = "valid.user@test.com";
    private static final String VALID_PASSWORD = "correctPassword123";
    private static final String WRONG_PASSWORD = "wrongpassword";
    private static final String NON_EXISTENT_EMAIL = "nonexistent.user@test.com";
    private static final String EMPTY_VALUE = "";
    private static final String UPPERCASE_VALID_EMAIL = "VALID.USER@TEST.COM";
    private static final String SQL_INJECTION_1 = " ' OR 1=1-- ";
    private static final String SQL_INJECTION_2 = " ' UNION SELECT * FROM users-- ";

    @DisplayName("test with valid email and password")
    @Test
    void testSuccessfulLoginWithValidCredentials() {

        AuthResponse response = userService.login(VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(VALID_EMAIL, response.getEmail());
        assertNotNull(response.getSessionToken());
        assertNull(response.getErrorMessage());
    }

    @DisplayName("test with invalid password")
    @Test
    void testFailedLoginWithWrongPassword() {
        AuthResponse response = userService.login(VALID_EMAIL, WRONG_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getSessionToken());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test with not existing email")
    @Test
    void testFailedLoginWithNoneExistedEmail() {
        AuthResponse response = userService.login(NON_EXISTENT_EMAIL, VALID_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getSessionToken());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test with empty email value")
    @Test
    void testFailedLoginWithEmptyEmail() {
        AuthResponse response = userService.login(EMPTY_VALUE, VALID_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getSessionToken());
        assertNotNull(response.getErrorMessage());

    }

    @DisplayName("test with empty password value")
    @Test
    void testFailedLoginWithEmptyPassword() {
        AuthResponse response = userService.login(VALID_EMAIL, EMPTY_VALUE);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getSessionToken());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test with null email")
    @Test
    void testFailedLoginWithNullEmail() {
        AuthResponse response = userService.login(null, VALID_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getSessionToken());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test with null password")
    @Test
    void testFailedLoginWithNullPassword() {
        AuthResponse response = userService.login(VALID_EMAIL, null);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getSessionToken());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test with sql-injection")
    @Test
    void testFailedLogInWithSqlInjection() {
        AuthResponse response = userService.login(SQL_INJECTION_1, EMPTY_VALUE);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getSessionToken());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test with sql-injection")
    @Test
    void testFailedLogInWithSqlInjectionUnion() {
        AuthResponse response = userService.login(SQL_INJECTION_2, EMPTY_VALUE);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getSessionToken());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test with valid uppercase email and password")
    @Test
    void testSuccessfulLoginWithUpperCaseEmail() {
        AuthResponse response = userService.login(UPPERCASE_VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(VALID_EMAIL, response.getEmail());
        assertNotNull(response.getSessionToken());
        assertNull(response.getErrorMessage());
    }
}




