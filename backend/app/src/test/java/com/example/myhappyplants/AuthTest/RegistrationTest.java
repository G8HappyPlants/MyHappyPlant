package com.example.myhappyplants.AuthTest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class RegistrationTest {

    @Mock
    private UserService userService;
    private static final String VALID_EMAIL = "VALID.USER@test.se";
    private static final String VALID_PASSWORD = "correctPassword123";
    private static final String EXISTING_EMAIL = "INVALID.USER@test.se";
    private static final String INVALID_FORMAT_EMAIL = "INVALID.USER@test.se";
    private static final String SHORT_PASSWORD = "kort12";
    private static final String SQL_INJECTION_EMAIL = "' OR 1=1 -- test.se";


    @BeforeEach
    void setUp(){

    }

    @Test
    void testRegistrationSuccess() {
        AuthResponse result = userService.register(VALID_EMAIL,VALID_PASSWORD);
        assertNotNull(result);

    }

    @Test
    void testRegistrationFailEmailExists() {
        AuthResponse result = userService.register(EXISTING_EMAIL, VALID_PASSWORD);
        assertNotNull(result);
    }

    @Test
    void testRegistrationFailInvalidEmailFormat() {
        AuthResponse result = userService.register(INVALID_FORMAT_EMAIL, VALID_PASSWORD);
        assertNotNull(result);
    }

    @Test
    void testRegistrationFailPasswordTooShort() {
        AuthResponse result = userService.register(VALID_EMAIL, SHORT_PASSWORD);
        assertNotNull(result);
    }

    @Test
    void testRegistrationFailPasswordsDoNotMatch() {
        AuthResponse result = userService.register(VALID_EMAIL, "password123");
        assertNotNull(result);
    }

    @Test
    void testRegistrationFailEmptyEmail() {
        AuthResponse result = userService.register("", VALID_PASSWORD);
        assertNotNull(result);
    }

    @Test
    void testRegistrationFailEmptyPassword() {
        AuthResponse result = userService.register(VALID_EMAIL, "");
        assertNotNull(result);
    }


    @Test
    void testRegistrationFailSqlInjection() {
        AuthResponse result = userService.register(SQL_INJECTION_EMAIL, VALID_PASSWORD);
        assertNotNull(result);
    }



}