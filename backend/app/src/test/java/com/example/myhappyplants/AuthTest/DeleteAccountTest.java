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

    private static final String USER = "testuser";
    private static final String VALID_EMAIL = "valid.user@test.com";
    private static final String VALID_PASSWORD = "correctPassword123!";
    private static final String INVALID_SESSION_TOKEN = "invalid-token";
    private static final String WRONG_PASSWORD = "wrongPassword";
    private static final String INVALID_EMAIL = "invalid@test.com";
    private static final String EMPTY_EMAIL = "";
    private static final String EMPTY_PASSWORD = "";
    private static final String EMPTY_TOKEN = "";


    @DisplayName("test account data is removed after deletion")
    @Test
    void testAccountDataIsRemovedAfterDeletion() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, VALID_PASSWORD);
        authService.deleteAccount(deleteRequest);

        LoginRequest secondLoginRequest = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
        assertThrows(Exception.class, () -> authService.login(secondLoginRequest));
    }

    @DisplayName("test failed account deletion with invalid session token")
    @Test
    void testFailedAccountDeletionWithInvalidToken() {
        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(INVALID_SESSION_TOKEN, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> {
            authService.deleteAccount(deleteRequest);
        });
    }

    @DisplayName("test failed account deletion with empty session token")
    @Test
    void testFailedAccountDeletionWithEmptyToken() {
        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(EMPTY_TOKEN, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> {
            authService.deleteAccount(deleteRequest);
        });
    }

    @DisplayName("test failed account deletion with null session token")
    @Test
    void testFailedAccountDeletionWithNullToken() {
        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(null, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> {
            authService.deleteAccount(deleteRequest);
        });
    }

    @DisplayName("test failed account deletion with wrong password")
    @Test
    void testFailedAccountDeletionWithWrongPassword() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, WRONG_PASSWORD);

        assertThrows(Exception.class, () -> {
            authService.deleteAccount(deleteRequest);
        });
    }

    @DisplayName("test failed account deletion with invalid email")
    @Test
    void testFailedAccountDeletionWithInvalidEmail() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, INVALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> {
            authService.deleteAccount(deleteRequest);
        });
    }

    @DisplayName("test failed account deletion with empty email")
    @Test
    void testFailedAccountDeletionWithEmptyEmail() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, EMPTY_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> {
            authService.deleteAccount(deleteRequest);
        });
    }

    @DisplayName("test failed account deletion with empty password")
    @Test
    void testFailedAccountDeletionWithEmptyPassword() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, EMPTY_PASSWORD);

        assertThrows(Exception.class, () -> {
            authService.deleteAccount(deleteRequest);
        });
    }

    @DisplayName("test failed account deletion with null email")
    @Test
    void testFailedAccountDeletionWithNullEmail() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, null, VALID_PASSWORD);

        assertThrows(Exception.class, () -> {
            authService.deleteAccount(deleteRequest);
        });
    }

    @DisplayName("test failed account deletion with null password")
    @Test
    void testFailedAccountDeletionWithNullPassword() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, VALID_EMAIL, null);

        assertThrows(Exception.class, () -> {
            authService.deleteAccount(deleteRequest);
        });
    }

    @DisplayName("test failed account deletion with valid token but mismatched email")
    @Test
    void testFailedAccountDeletionWithMismatchedEmail() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String sessionToken = loginResponse.getToken();

        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(sessionToken, "another.user@test.com", VALID_PASSWORD);

        assertThrows(Exception.class, () -> {
            authService.deleteAccount(deleteRequest);
        });
    }
}
