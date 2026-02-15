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
    private static final String EMAIL_INVALID_FORMAT = "valid.usertest.com";
    private static final String VALID_PASSWORD = "correctPassword123!";
    private static final String WRONG_PASSWORD = "WrongPassword123!";
    private static final String NON_EXISTENT_EMAIL = "nonexistent.user@test.com";
    private static final String EMPTY_VALUE = "";
    private static final String WHITE_SPACE = "  ";
    private static final String UPPERCASE_EMAIL = "VALID.USER@TEST.COM";
    private static final String SQL_INJECTION = " ' OR 1=1-- ";
    private static final String EMAIL_WITH_WHITESPACE = " valid.user@test.com ";

    @BeforeEach
    void setUp() {
        authService.register(new RegisterRequest(USERNAME, VALID_EMAIL, VALID_PASSWORD));
    }

    /**
     * ================== POSITIVE TEST CASES
     */
    @DisplayName("test with valid email and password - ANV-01-F-1")
    @Test
    void testSuccessfulLoginWithValidCredentials() {
        LoginRequest request = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @DisplayName("test with valid uppercase email - ANV-01-F-2")
    @Test
    void testSuccessfulLoginWithUppercaseEmail() {
        LoginRequest request = new LoginRequest(UPPERCASE_EMAIL, VALID_PASSWORD);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @DisplayName("test with email containing whitespace - ANV-01-F-3")
    @Test
    void testSuccessfulLoginWithEmailWhitespace() {
        LoginRequest request = new LoginRequest(EMAIL_WITH_WHITESPACE, VALID_PASSWORD);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @DisplayName("test with leading whitespace in email - ANV-01-F-4")
    @Test
    void testSuccessfulLoginWithLeadingWhitespaceEmail() {
        LoginRequest request = new LoginRequest(WHITE_SPACE + VALID_EMAIL, VALID_PASSWORD);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @DisplayName("test with trailing whitespace in email - ANV-01-F-5")
    @Test
    void testSuccessfulLoginWithTrailingWhitespaceEmail() {
        LoginRequest request = new LoginRequest(VALID_EMAIL + WHITE_SPACE, VALID_PASSWORD);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    /**
     * ============== NEGATIVE TEST CASES - WRONG CREDENTIALS
     */
    @DisplayName("test with wrong password - ANV-01-F-6")
    @Test
    void testFailedLoginWithWrongPassword() {
        LoginRequest request = new LoginRequest(VALID_EMAIL, WRONG_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with non-existent email - ANV-01-F-7")
    @Test
    void testFailedLoginWithNonExistentEmail() {
        LoginRequest request = new LoginRequest(NON_EXISTENT_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with wrong password and wrong email - ANV-01-F-7")
    @Test
    void testFailedLoginWithWrongEmailAndPassword() {
        LoginRequest request = new LoginRequest(NON_EXISTENT_EMAIL, WRONG_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with email without @ symbol - ANV-01-F-9")
    @Test
    void testFailedLoginWithMultipleAtSymbols() {
        LoginRequest request = new LoginRequest(EMAIL_INVALID_FORMAT, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    /**
     * ============== NEGATIVE TEST CASES - EMPTY VALUES
     */

    @DisplayName("test with empty email - ANV-01-F-10")
    @Test
    void testFailedLoginWithEmptyEmail() {
        LoginRequest request = new LoginRequest(EMPTY_VALUE, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with empty password - ANV-01-F-11")
    @Test
    void testFailedLoginWithEmptyPassword() {
        LoginRequest request = new LoginRequest(VALID_EMAIL, EMPTY_VALUE);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with both empty email and password - ANV-01-F-12")
    @Test
    void testFailedLoginWithBothEmpty() {
        LoginRequest request = new LoginRequest(EMPTY_VALUE, EMPTY_VALUE);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with whitespace-only email - ANV-01-F-13")
    @Test
    void testFailedLoginWithWhitespaceOnlyEmail() {
        LoginRequest request = new LoginRequest(WHITE_SPACE, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with whitespace-only password - ANV-01-F-14")
    @Test
    void testFailedLoginWithWhitespaceOnlyPassword() {
        LoginRequest request = new LoginRequest(VALID_EMAIL, WHITE_SPACE);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    /**
     *  ================ NEGATIVE TEST CASES - NULL VALES
     */

    @DisplayName("test with null email - ANV-01-F-15")
    @Test
    void testFailedLoginWithNullEmail() {
        LoginRequest request = new LoginRequest(null, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with null password - ANV-01-F-16")
    @Test
    void testFailedLoginWithNullPassword() {
        LoginRequest request = new LoginRequest(VALID_EMAIL, null);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with both null email and password - ANV-01-F-17")
    @Test
    void testFailedLoginWithBothNull() {
        LoginRequest request = new LoginRequest(null, null);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with null LoginRequest object - ANV-01-F-18")
    @Test
    void testFailedLoginWithNullObject() {
        assertThrows(Exception.class, () -> authService.login(null));
    }

    /**
     * =========0 SECURITY TEST CASES - SQL INJECTION
     */

    @DisplayName("test with SQL injection in email - ANV-01-F-19")
    @Test
    void testFailedLoginWithSqlInjectionInEmail() {
        LoginRequest request = new LoginRequest(SQL_INJECTION, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with SQL injection in password - ANV-01-F-20")
    @Test
    void testFailedLoginWithSqlInjectionInPassword() {
        LoginRequest request = new LoginRequest(VALID_EMAIL, SQL_INJECTION);

        assertThrows(Exception.class, () -> authService.login(request));
    }

    @DisplayName("test with SQL injection in both fields - ANV-01-F-21")
    @Test
    void testFailedLoginWithSqlInjectionInBothFields() {
        LoginRequest request = new LoginRequest(SQL_INJECTION, SQL_INJECTION);

        assertThrows(Exception.class, () -> authService.login(request));
    }
}




