package com.example.myhappyplants.AuthTest;

import lombok.Getter;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {

    @Mock
    private UserService userService;
    private static final String VALID_EMAIL = "valid.user@test.com";
    private static final String VALID_PASSWORD = "correctPassword123";
    private static final String WRONG_PASSWORD = "wrongpassword";
    private static final String NON_EXISTENT_EMAIL = "nonexistent.user@test.com";


    @BeforeEach
    void setUp() {

    }

    @Test
    void testSuccessfulLoginWithValidCredentials() {

        AuthResponse result = userService.login(VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(result);

    }





}

interface UserService {
    AuthResponse login(String email, String password);
}

@Getter
class AuthResponse {
    private boolean success;
    private String email;
    private String sessionToken;
    private String errorMessage;

    private AuthResponse(boolean success, String email, String sessionToken, String errorMessage) {
        this.success = success;
        this.email = email;
        this.sessionToken = sessionToken;
        this.errorMessage = errorMessage;
    }

    public static AuthResponse success(String email, String token) {
        return new AuthResponse(true, email, token, null);
    }

    public static AuthResponse failure(String error) {
        return new AuthResponse(false, null, null, error);
    }

}




