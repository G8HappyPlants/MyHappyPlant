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

    private static final String VALID_USERNAME = "testuser";
    private static final String VALID_EMAIL = "valid.user@test.com";
    private static final String VALID_PASSWORD = "correctPassword123";
    private static final String INVALID_TOKEN = "invalid-token-12345";
    private static final String EMPTY_VALUE = "";
    private static final String WHITE_SPACE = "   ";
    private static final String SQL_INJECTION = "' OR 1=1--";

    @BeforeEach
    void setUp() {
        // Clean database

        // Register test user for logout tests
        authService.register(new RegisterRequest(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD));
    }

    @AfterEach
    void tearDown() {
        // clean the database
    }

    /**
     * ==============0 POSITIVE TEST CASES
     */
    @DisplayName("test successful logout with valid session token - ANV-02-F-1")
    @Test
    void testSuccessfulLogoutWithValidToken() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());
        String token = loginResponse.getToken();

        LogoutRequest logoutRequest = new LogoutRequest(token);
        authService.logout(logoutRequest);

        assertDoesNotThrow(() -> authService.logout(logoutRequest));
    }

    @DisplayName("test user can login again after logout - ANV-02-F-2")
    @Test
    void testUserCanLoginAgainAfterLogout() {
        AuthResponse firstLogin = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String firstToken = firstLogin.getToken();

        // Logout
        LogoutRequest logoutRequest = new LogoutRequest(firstToken);
        authService.logout(logoutRequest);

        // Login again should work
        AuthResponse secondLogin = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        assertNotNull(secondLogin);
        assertNotNull(secondLogin.getToken());
    }

    @DisplayName("test logout on one device does not affect other sessions - ANV-02-F-3")
    @Test
    void testLogoutDoesNotAffectOtherSessions() {
        // First login (Device 1)
        AuthResponse firstLogin = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String firstToken = firstLogin.getToken();

        // Second login (Device 2
        AuthResponse secondLogin = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String secondToken = secondLogin.getToken();

        // Logout from Device 1
        LogoutRequest firstLogout = new LogoutRequest(firstToken);
        authService.logout(firstLogout);

        // Device 2 session should still be valid
        LogoutRequest secondLogout = new LogoutRequest(secondToken);
        assertDoesNotThrow(() -> authService.logout(secondLogout));
    }

    /**
     * ===================== NEGATIVE TEST CASES - INVALID TOKEN
     */
    @DisplayName("test failed logout with invalid token - ANV-02-F-4")
    @Test
    void testFailedLogoutWithInvalidToken() {
        LogoutRequest logoutRequest = new LogoutRequest(INVALID_TOKEN);

        assertThrows(Exception.class, () -> authService.logout(logoutRequest));
    }

    @DisplayName("test failed logout with empty token - ANV-02-F-5")
    @Test
    void testFailedLogoutWithEmptyToken() {
        LogoutRequest logoutRequest = new LogoutRequest(EMPTY_VALUE);

        assertThrows(Exception.class, () -> authService.logout(logoutRequest));
    }

    @DisplayName("test failed logout with whitespace-only token - ANV-02-F-6")
    @Test
    void testFailedLogoutWithWhitespaceOnlyToken() {
        LogoutRequest logoutRequest = new LogoutRequest(WHITE_SPACE);

        assertThrows(Exception.class, () -> authService.logout(logoutRequest));
    }

    @DisplayName("test failed logout with null token - ANV-02-F-7")
    @Test
    void testFailedLogoutWithNullToken() {
        LogoutRequest logoutRequest = new LogoutRequest(null);

        assertThrows(Exception.class, () -> authService.logout(logoutRequest));
    }

    @DisplayName("test failed logout with null object - ANV-02-F-8")
    @Test
    void testFailedLogoutWithNullObject() {
        assertThrows(Exception.class, () -> authService.logout(null));
    }

    /**
     * ======================== EDGE TEST CASES
     */
    @DisplayName("test logout after account deletion - ANV-02-F-9")
    @Test
    void testLogoutAfterAccountDeletion() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String token = loginResponse.getToken();


        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(token, VALID_EMAIL, VALID_PASSWORD);
        authService.deleteAccount(deleteRequest);

        LogoutRequest logoutRequest = new LogoutRequest(token);
        assertThrows(Exception.class, () -> authService.logout(logoutRequest));
    }

    @DisplayName("test logout with token containing leading whitespace - ANV-02-F-10")
    @Test
    void testFailedLogoutWithTokenLeadingWhitespace() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String token = loginResponse.getToken();
        String tokenWithWhitespace = WHITE_SPACE + token;

        LogoutRequest logoutRequest = new LogoutRequest(tokenWithWhitespace);

        assertThrows(Exception.class, () -> authService.logout(logoutRequest));
    }

    @DisplayName("test logout with token containing trailing whitespace - ANV-02-F-11")
    @Test
    void testFailedLogoutWithTokenTrailingWhitespace() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String token = loginResponse.getToken();
        String tokenWithWhitespace = token + WHITE_SPACE;

        LogoutRequest logoutRequest = new LogoutRequest(tokenWithWhitespace);

        assertThrows(Exception.class, () -> authService.logout(logoutRequest));
    }

    /**
     *  ==================== SECURITY TEST CASES - SQL INJECTION
     */
    @DisplayName("test failed logout with SQL injection in token - ANV-02-F-12")
    @Test
    void testFailedLogoutWithSqlInjectionInToken() {
        LogoutRequest logoutRequest = new LogoutRequest(SQL_INJECTION);

        assertThrows(Exception.class, () -> authService.logout(logoutRequest));
    }

    /**
     * =======================  VALIDATE TOKEN
     */
    @DisplayName("test multiple logout attempts with same token - ANV-02-F-13")
    @Test
    void testMultipleLogoutAttemptsWithSameToken() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));

        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());
        String token = loginResponse.getToken();

        LogoutRequest logoutRequest = new LogoutRequest(token);
        authService.logout(logoutRequest);

        assertThrows(Exception.class, () -> {
            authService.logout(logoutRequest);
        });
    }

    @DisplayName("test token cannot be used for other operations after logout - ANV-02-F-14")
    @Test
    void testTokenInvalidAfterLogout() {
        AuthResponse loginResponse = authService.login(new LoginRequest(VALID_EMAIL, VALID_PASSWORD));
        String token = loginResponse.getToken();

        // logout
        LogoutRequest logoutRequest = new LogoutRequest(token);
        authService.logout(logoutRequest);

        // Try to use the logged out token for account deletion
        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(token, VALID_EMAIL, VALID_PASSWORD);
        assertThrows(Exception.class, () -> authService.deleteAccount(deleteRequest));
    }

}