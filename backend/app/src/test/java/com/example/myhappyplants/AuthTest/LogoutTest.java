package com.example.myhappyplants.AuthTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LogoutTest {

    @Mock
    private UserService userService;

    private static final String VALID_EMAIL = "valid.user@test.com";
    private static final String VALID_PASSWORD = "correctPassword123";
    private static final String INVALID_SESSION_TOKEN = "invalid-token";
    private static final String EXPIRED_SESSION_TOKEN = "expired-token-67890";
    private static final String EMPTY_TOKEN = "";


    @DisplayName("test successful logout with valid session token")
    @Test
    void testSuccessfulLogoutWithValidToken() {

        AuthResponse loginResponse = userService.login(VALID_EMAIL, VALID_PASSWORD);
        assertTrue(loginResponse.isSuccess());
        String sessionToken = loginResponse.getSessionToken();

        AuthResponse response = userService.logout(sessionToken);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNull(response.getSessionToken());
        assertNull(response.getErrorMessage());
    }

    @DisplayName("test successful logout with valid session token")
    @Test
    void testFailedLogoutWithInvalidToken() {
        AuthResponse response = userService.logout(INVALID_SESSION_TOKEN);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test failed logout with expired session token")
    @Test
    void testFailedLogoutWithExpiredToken() {
        AuthResponse response = userService.logout(EXPIRED_SESSION_TOKEN);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test failed logout with empty token")
    @Test
    void testFailedLogoutWithEmptyToken() {
        AuthResponse response = userService.logout(EMPTY_TOKEN);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test failed logout with null token")
    @Test
    void testFailedLogoutWithNullToken() {
        AuthResponse response = userService.logout(null);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }

    @DisplayName("test multiple logout attempts with same token")
    @Test
    void testMultipleLogoutAttemptsWithSameToken() {

        AuthResponse loginResponse = userService.login(VALID_EMAIL, VALID_PASSWORD);
        assertTrue(loginResponse.isSuccess());
        String sessionToken = loginResponse.getSessionToken();

        AuthResponse firstLogoutResponse = userService.logout(sessionToken);
        assertNotNull(firstLogoutResponse);
        assertTrue(firstLogoutResponse.isSuccess());
        assertNull(firstLogoutResponse.getErrorMessage());


        AuthResponse secondLogoutResponse = userService.logout(sessionToken);
        assertNotNull(secondLogoutResponse);
        assertFalse(secondLogoutResponse.isSuccess());
        assertNotNull(secondLogoutResponse.getErrorMessage());
    }

}
