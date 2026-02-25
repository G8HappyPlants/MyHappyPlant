package com.example.myhappyplants.auth;

import com.example.myhappyplants.entity.BlacklistedJwtToken;
import com.example.myhappyplants.repository.TokenBlacklistRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private TokenBlacklistRepository repo;
    private JwtService jwtService;

    @BeforeEach
    void setup() {
        repo = mock(TokenBlacklistRepository.class);

        String secret = "12345678901234567890123456789012"; // 32+ bytes
        long expirationMinutes = 60;

        jwtService = new JwtService(secret, expirationMinutes, repo);
    }

    @Test
    void destroyToken_savesTokenInBlacklist() {
        String email = "user@test.com";
        String token = jwtService.createToken(email);

        jwtService.destroyToken(token);

        ArgumentCaptor<BlacklistedJwtToken> captor =
                ArgumentCaptor.forClass(BlacklistedJwtToken.class);

        verify(repo).save(captor.capture());

        BlacklistedJwtToken saved = captor.getValue();
        assertEquals(token, saved.getTokenId());
        assertTrue(saved.getExpiresAt().isAfter(Instant.now()));
    }

    @Test
    void isTokenValid_returnsFalseIfTokenBlacklisted() {
        String email = "user@test.com";
        String token = jwtService.createToken(email);

        when(repo.existsByTokenId(token)).thenReturn(false);
        assertTrue(jwtService.isTokenValid(token, email));

        when(repo.existsByTokenId(token)).thenReturn(true);
        assertFalse(jwtService.isTokenValid(token, email));
    }

    @Test
    void destroyToken_withInvalidJwt_throwsException() {
        assertThrows(Exception.class,
                () -> jwtService.destroyToken("not-a-valid-jwt"));
    }
}