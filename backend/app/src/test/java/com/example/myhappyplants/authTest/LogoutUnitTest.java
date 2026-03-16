package com.example.myhappyplants.authTest;

import com.example.myhappyplants.auth.JwtService;
import com.example.myhappyplants.repository.UserRepository;
import com.example.myhappyplants.service.AuthService;
import com.example.myhappyplants.service.CryptoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutUnitTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtService jwtService;
    @Mock
    CryptoService cryptoService;

    @InjectMocks
    AuthService authService;

    private static final String VALID_TOKEN = "valid.jwt.token";
    private static final String INVALID_TOKEN = "invalid-token-12345";
    private static final String EMPTY_VALUE = "";
    private static final String WHITE_SPACE = "   ";
    private static final String SQL_INJECTION = "' OR 1=1--";

    @DisplayName("test successful logout with valid session token - ANV-02-F-1")
    @Test
    void testSuccessfulLogoutWithValidToken() {
        // TESTAR: AuthService tar token från Authentication.getCredentials()
        // och skickar vidare till jwtService.destroyToken utan att kasta exception.
        Authentication auth = mock(Authentication.class);
        when(auth.getCredentials()).thenReturn(VALID_TOKEN);

        assertDoesNotThrow(() -> authService.logout(auth));

        verify(jwtService).destroyToken(VALID_TOKEN);
    }

    @DisplayName("test failed logout with invalid token - ANV-02-F-4")
    @Test
    void testFailedLogoutWithInvalidToken() {
        // TESTAR: Om credentials är en ogiltig token och JwtService kastar, ska exception bubbla upp.
        Authentication auth = mock(Authentication.class);
        when(auth.getCredentials()).thenReturn(INVALID_TOKEN);

        doThrow(new RuntimeException("Invalid JWT")).when(jwtService).destroyToken(INVALID_TOKEN);

        assertThrows(Exception.class, () -> authService.logout(auth));
    }

    @DisplayName("test failed logout with empty token - ANV-02-F-5")
    @Test
    void testFailedLogoutWithEmptyToken() {
        // TESTAR (unit-variant): Om vi *ändå* hamnar i AuthService med tom credentials,
        // så kastar JwtService (parsing fail). I riktiga API:t blockas detta oftast innan service.
        Authentication auth = mock(Authentication.class);
        when(auth.getCredentials()).thenReturn(EMPTY_VALUE);

        doThrow(new RuntimeException("Invalid JWT")).when(jwtService).destroyToken(EMPTY_VALUE);

        assertThrows(Exception.class, () -> authService.logout(auth));
    }

    @DisplayName("test failed logout with whitespace-only token - ANV-02-F-6")
    @Test
    void testFailedLogoutWithWhitespaceOnlyToken() {
        // TESTAR (unit-variant): whitespace credentials -> JwtService kastar.
        Authentication auth = mock(Authentication.class);
        when(auth.getCredentials()).thenReturn(WHITE_SPACE);

        doThrow(new RuntimeException("Invalid JWT")).when(jwtService).destroyToken(WHITE_SPACE);

        assertThrows(Exception.class, () -> authService.logout(auth));
    }

    @DisplayName("test failed logout with null token - ANV-02-F-7")
    @Test
    void testFailedLogoutWithNullToken() {
        //TA HEADER/BEARER token, och nulla den istället??? ta bort och gör integration test ist
        Authentication auth = mock(Authentication.class);
        when(auth.getCredentials()).thenReturn(null);

        // Mocka beteendet som verkliga JwtService skulle ha
        doThrow(new IllegalArgumentException("Token cannot be null"))
                .when(jwtService).destroyToken(null);

        assertThrows(IllegalArgumentException.class,
                () -> authService.logout(auth));
    }

    @DisplayName("test failed logout with null Authentication object - ANV-02-F-8")
    @Test
    void testFailedLogoutWithNullObject() {
        // TESTAR: authService.logout(null) kastar (NullPointerException)
        assertThrows(Exception.class, () -> authService.logout(null));
    }

    @DisplayName("test failed logout with SQL injection in token - ANV-02-F-12")
    @Test
    void testFailedLogoutWithSqlInjectionInToken() {
        // TESTAR: SQL-injection sträng är inte JWT -> JwtService kastar.
        Authentication auth = mock(Authentication.class);
        when(auth.getCredentials()).thenReturn(SQL_INJECTION);

        doThrow(new RuntimeException("Invalid JWT")).when(jwtService).destroyToken(SQL_INJECTION);

        assertThrows(Exception.class, () -> authService.logout(auth));
    }
}