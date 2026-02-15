package com.example.myhappyplants.AuthTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteAccountTest {

    @Mock
    private AuthService authService;

    private static final String VALID_EMAIL = "valid.user@test.com";
    private static final String VALID_PASSWORD = "correctPassword123!";
    private static final String WRONG_PASSWORD = "WrongPassword123!";
    private static final String INVALID_EMAIL = "invalid@test.com";
    private static final String INVALID_SESSION_TOKEN = "invalid-token-12345";
    private static final String EMPTY_VALUE = "";
    private static final String WHITE_SPACE = "   ";
    private static final String UPPERCASE_EMAIL = "VALID.USER@TEST.COM";
    private static final String EMAIL_WITH_WHITESPACE = " valid.user@test.com ";
    private static final String SQL_INJECTION = "' OR 1=1--";


    /**
     * =================== POSITIVE TEST CASES
     */
    @DisplayName("test successful account deletion with valid credentials - ANV-04-F-1")
    @Test
    void testSuccessfulAccountDeletion() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, VALID_PASSWORD);
        authService.deleteAccount(deleteRequest);

        LoginRequest secondLoginRequest = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
        assertThrows(Exception.class, () -> authService.login(secondLoginRequest));
    }

    @DisplayName("test successful account deletion with uppercase email - ANV-04-F-2")
    @Test
    void testSuccessfulAccountDeletionWithUppercaseEmail() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, UPPERCASE_EMAIL, VALID_PASSWORD);
        authService.deleteAccount(deleteRequest);

        LoginRequest secondLoginRequest = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
        assertThrows(Exception.class, () -> authService.login(secondLoginRequest));
    }

    @DisplayName("test successful account deletion with email containing whitespace - ANV-04-F-3")
    @Test
    void testSuccessfulAccountDeletionWithEmailWhitespace() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, EMAIL_WITH_WHITESPACE, VALID_PASSWORD);
        authService.deleteAccount(deleteRequest);

        LoginRequest secondLoginRequest = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
        assertThrows(Exception.class, () -> authService.login(secondLoginRequest));
    }

    /**
     * ================== NEGATIVE TEST CASES - INVALID TOKEN
     */
    @DisplayName("test failed account deletion with invalid session token - ANV-04-F-4")
    @Test
    void testFailedAccountDeletionWithInvalidToken() {
        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(INVALID_SESSION_TOKEN, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with empty session token - ANV-04-F-5")
    @Test
    void testFailedAccountDeletionWithEmptyToken() {
        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(EMPTY_VALUE, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with null session token - ANV-04-F-6")
    @Test
    void testFailedAccountDeletionWithNullToken() {
        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(null, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with whitespace-only token - ANV-04-F-7")
    @Test
    void testFailedAccountDeletionWithWhitespaceOnlyToken() {
        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(WHITE_SPACE, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    /**
     * ============ NEGATIVE TEST CASES - WRONG CREDENTIALS
     */

    @DisplayName("test failed account deletion with wrong password - ANV-04-F-8")
    @Test
    void testFailedAccountDeletionWithWrongPassword() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, WRONG_PASSWORD);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with invalid email - ANV-04-F-9")
    @Test
    void testFailedAccountDeletionWithInvalidEmail() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, INVALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    /**
     * =============  NEGATIVE TEST CASES - EMPTY VALUES
     */

    @DisplayName("test failed account deletion with empty email - ANV-04-F-10")
    @Test
    void testFailedAccountDeletionWithEmptyEmail() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, EMPTY_VALUE, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with empty password - ANV-04-F-11")
    @Test
    void testFailedAccountDeletionWithEmptyPassword() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, EMPTY_VALUE);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with all empty values - ANV-04-F-12")
    @Test
    void testFailedAccountDeletionWithAllEmpty() {
        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with whitespace-only email - ANV-04-F-13")
    @Test
    void testFailedAccountDeletionWithWhitespaceOnlyEmail() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, WHITE_SPACE, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with whitespace-only password - ANV-04-F-14")
    @Test
    void testFailedAccountDeletionWithWhitespaceOnlyPassword() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, WHITE_SPACE);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    /**
     * =================0 NEGATIVE TEST CASES - NULL VALUES
     */
    @DisplayName("test failed account deletion with null email - ANV-04-F-15")
    @Test
    void testFailedAccountDeletionWithNullEmail() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, null, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with null password - ANV-04-F-16")
    @Test
    void testFailedAccountDeletionWithNullPassword() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, null);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with all null values - ANV-04-F-17")
    @Test
    void testFailedAccountDeletionWithAllNull() {
        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(null, null, null);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with null object - ANV-04-F-18")
    @Test
    void testFailedAccountDeletionWithNullObject() {
        assertThrows(Exception.class, () -> authService.deleteAccount(null));
    }

    /**
     * ================ SECURITY TEST CASES - SQL INJECTION
     */
    @DisplayName("test failed account deletion with SQL injection in email - ANV-04-F-19")
    @Test
    void testFailedAccountDeletionWithSqlInjectionInEmail() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, SQL_INJECTION, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    @DisplayName("test failed account deletion with SQL injection in password - ANV-04-F-20")
    @Test
    void testFailedAccountDeletionWithSqlInjectionInPassword() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, SQL_INJECTION);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

    /**
     * ==================== EDGE TEST CASES
     */
    @DisplayName("test account cannot be deleted twice - ANV-04-F-21")
    @Test
    void testAccountCannotBeDeletedTwice() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, VALID_PASSWORD);
        authService.deleteAccount(deleteRequest);

        DeleteAccountRequest secondDeleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, VALID_PASSWORD);
        assertThrows(Exception.class, () -> authService.deleteAccount(secondDeleteRequest));
    }

    @DisplayName("test account deletion with token from different user - ANV-04-F-22")
    @Test
    void testFailedAccountDeletionWithTokenFromDifferentUser() {
        String secondEmail = "second.user@test.com";
        authService.register(new RegisterRequest("seconduser", secondEmail, VALID_PASSWORD));
        AuthResponse secondUserLogin = authService.login(new LoginRequest(secondEmail, VALID_PASSWORD));
        String secondUserToken = secondUserLogin.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(secondUserToken, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }
}