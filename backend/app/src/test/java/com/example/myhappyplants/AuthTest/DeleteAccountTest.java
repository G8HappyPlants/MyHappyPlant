package com.example.myhappyplants.AuthTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteAccountTest {

    @Mock
    private UserService userService;

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

        AuthResponse loginResponse = userService.login(VALID_EMAIL, VALID_PASSWORD);
        assertTrue(loginResponse.isSuccess());
        String sessionToken = loginResponse.getSessionToken();


        AuthResponse deleteResponse = userService.deleteAccount(sessionToken, VALID_EMAIL, VALID_PASSWORD);
        assertNotNull(deleteResponse);
        assertTrue(deleteResponse.isSuccess());
        assertNull(deleteResponse.getErrorMessage());

        AuthResponse secondLoginResponse = userService.login(VALID_EMAIL, VALID_PASSWORD);
        assertFalse(secondLoginResponse.isSuccess());
        assertNotNull(secondLoginResponse.getErrorMessage());
    }


    @DisplayName("test failed account deletion with invalid session token")
    @Test
    void testFailedAccountDeletionWithInvalidToken() {
        AuthResponse response = userService.deleteAccount(INVALID_SESSION_TOKEN, VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }


    @DisplayName("test failed account deletion with empty session token")
    @Test
    void testFailedAccountDeletionWithEmptyToken() {
        AuthResponse response = userService.deleteAccount(EMPTY_TOKEN, VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test failed account deletion with null session token")
    @Test
    void testFailedAccountDeletionWithNullToken() {
        AuthResponse response = userService.deleteAccount(null, VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test failed account deletion with wrong password")
    @Test
    void testFailedAccountDeletionWithWrongPassword() {
        AuthResponse loginResponse = userService.login(VALID_EMAIL, VALID_PASSWORD);
        String sessionToken = loginResponse.getSessionToken();

        AuthResponse response = userService.deleteAccount(sessionToken, VALID_EMAIL, WRONG_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test failed account deletion with invalid email")
    @Test
    void testFailedAccountDeletionWithInvalidEmail() {
        AuthResponse loginResponse = userService.login(VALID_EMAIL, VALID_PASSWORD);
        String sessionToken = loginResponse.getSessionToken();

        AuthResponse response = userService.deleteAccount(sessionToken, INVALID_EMAIL, VALID_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test failed account deletion with empty email")
    @Test
    void testFailedAccountDeletionWithEmptyEmail() {
        AuthResponse loginResponse = userService.login(VALID_EMAIL, VALID_PASSWORD);
        String sessionToken = loginResponse.getSessionToken();

        AuthResponse response = userService.deleteAccount(sessionToken, EMPTY_EMAIL, VALID_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test failed account deletion with empty password")
    @Test
    void testFailedAccountDeletionWithEmptyPassword() {
        AuthResponse loginResponse = userService.login(VALID_EMAIL, VALID_PASSWORD);
        String sessionToken = loginResponse.getSessionToken();

        AuthResponse response = userService.deleteAccount(sessionToken, VALID_EMAIL, EMPTY_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test failed account deletion with null email")
    @Test
    void testFailedAccountDeletionWithNullEmail() {
        AuthResponse loginResponse = userService.login(VALID_EMAIL, VALID_PASSWORD);
        String sessionToken = loginResponse.getSessionToken();

        AuthResponse response = userService.deleteAccount(sessionToken, null, VALID_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test failed account deletion with null password")
    @Test
    void testFailedAccountDeletionWithNullPassword() {

        AuthResponse loginResponse = userService.login(VALID_EMAIL, VALID_PASSWORD);
        String sessionToken = loginResponse.getSessionToken();


        AuthResponse response = userService.deleteAccount(sessionToken, VALID_EMAIL, null);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test failed account deletion with valid token but mismatched email")
    @Test
    void testFailedAccountDeletionWithMismatchedEmail() {
        AuthResponse loginResponse = userService.login(VALID_EMAIL, VALID_PASSWORD);
        String sessionToken = loginResponse.getSessionToken();

        AuthResponse response = userService.deleteAccount(sessionToken, "another.user@test.com", VALID_PASSWORD);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }
}
