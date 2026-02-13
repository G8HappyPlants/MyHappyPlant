package com.example.myhappyplants.AuthTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LogoutTest {

    @Mock
    private AuthService authService;

    private static final String VALID_EMAIL = "valid.user@test.com";
    private static final String VALID_PASSWORD = "correctPassword123";
    private static final String INVALID_SESSION_TOKEN = "invalid-token";
    private static final String EMPTY_TOKEN = "";


    @DisplayName("test successful logout with valid session token")
    @Test
    void testSuccessfulLogoutWithValidToken() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));

        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());
        String sessionToken = loginResponse.getToken();

        LogoutRequest logoutRequest = new LogoutRequest(sessionToken);
        authService.logout(logoutRequest);
    }

    @DisplayName("test successful logout with valid session token")
    @Test
    void testFailedLogoutWithInvalidToken() {
        LogoutRequest logoutRequest = new LogoutRequest(INVALID_SESSION_TOKEN);

        assertThrows(Exception.class, () -> {
            authService.logout(logoutRequest);
        });
    }

    @DisplayName("test failed logout with empty token")
    @Test
    void testFailedLogoutWithEmptyToken() {
        LogoutRequest logoutRequest = new LogoutRequest(EMPTY_TOKEN);

        assertThrows(Exception.class, () -> {
            authService.logout(logoutRequest);
        });
    }

    @DisplayName("test failed logout with null token")
    @Test
    void testFailedLogoutWithNullToken() {
        LogoutRequest logoutRequest = new LogoutRequest(null);

        assertThrows(Exception.class, () -> {
            authService.logout(logoutRequest);
        });
    }

    @DisplayName("test multiple logout attempts with same token")
    @Test
    void testMultipleLogoutAttemptsWithSameToken() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));

        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());
        String sessionToken = loginResponse.getToken();

        LogoutRequest logoutRequest = new LogoutRequest(sessionToken);
        authService.logout(logoutRequest);

        assertThrows(Exception.class, () -> {
            authService.logout(logoutRequest);
        });
    }

}