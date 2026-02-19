package com.example.myhappyplants.auth;

import com.example.myhappyplants.entity.BlacklistedJwtToken;
import com.example.myhappyplants.repository.TokenBlacklistRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;

/**
 * Service ansvarig för att:
 *  - skapa JWT tokens
 *  - läsa (parsa) JWT tokens
 *  - validera JWT tokens
 *
 * Används av:
 *  - AuthService (vid registrering)
 *  - JwtAuthenticationFilter (vid varje request)
 */
@Service
public class JwtService {

    //Secret key - used to sign JWT when token is created
    //and verify JWT when token is read - is created from app.jwt.secret in yml-file
    private final SecretKey key;

    //For how long the token will be valid
    private final long expirationMinutes;

	private TokenBlacklistRepository tokenBlacklistRepository;

    //konstruktor som körs av Spring.
    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-minutes}") long expirationMinutes,
			TokenBlacklistRepository tokenBlacklistRepository
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;

		this.tokenBlacklistRepository = tokenBlacklistRepository;
    }
    /**
     * Skapar en JWT token för en användare.
     *
     * @param email användarens email (används som "subject" i token)
     * @return signerad JWT token
     */
    public String createToken(String email) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationMinutes * 60);

        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    //Checks if a JWT token is valid (and hasn't expired)
    //token is correctly signed, email in token matches expected email,
    public boolean isTokenValid(String token, String expectedEmail) {
        Claims claims = parseClaims(token);
        String tokenEmail = claims.getSubject();
        Date expiration = claims.getExpiration();

        return tokenEmail != null
                && tokenEmail.equals(expectedEmail)
                && expiration != null
                && expiration.after(new Date())
				&& !tokenBlacklistRepository.existsByTokenId(token);
    }

    //Intern helpfuntion that:
    //verifies signature, parse JWT, returns all claims
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public void destroyToken(String jwtToken) {
        Claims claims = parseClaims(jwtToken);
        Date expiration = claims.getExpiration();
        tokenBlacklistRepository.save(new BlacklistedJwtToken(jwtToken,expiration.toInstant()));
    }
}
