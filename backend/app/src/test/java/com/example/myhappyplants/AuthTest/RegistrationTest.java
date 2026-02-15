package com.example.myhappyplants.AuthTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationTest {

    @Mock
    private AuthService authService;

    private static final String VALID_USERNAME = "testuser";
    private static final String VALID_EMAIL = "valid.user@test.com";
    private static final String VALID_PASSWORD = "ValidPass123!";
    private static final String SHORT_USERNAME = "ab";
    private static final String SHORT_PASSWORD = "Pass1!";
    private static final String WEAK_PASSWORD_NO_UPPERCASE = "weakpass123!";
    private static final String WEAK_PASSWORD_NO_LOWERCASE = "WEAKPASS123!";
    private static final String WEAK_PASSWORD_NO_DIGIT = "WeakPassword!";
    private static final String WEAK_PASSWORD_ONE_DIGIT = "WeakPassword1!";
    private static final String WEAK_PASSWORD_NO_SPECIAL = "WeakPass123";
    private static final String INVALID_EMAIL = "invalid.email.com";
    private static final String EMPTY_VALUE = "";
    private static final String UPPERCASE_EMAIL = "VALID.USER@TEST.COM";
    private static final String EMAIL_WITH_WHITESPACE = " valid.user@test.com ";
    private static final String USERNAME_WITH_WHITESPACE = " testuser ";
    private static final String SQL_INJECTION = " ' OR 1=1-- ";
    private static final String EXISTING_EMAIL = "existing@test.com";
    private static final String EXISTING_USERNAME = "existinguser";
    private static final String WHITE_SPACE = "  ";

    @BeforeEach
    void setUp() {
        authService.register(new RegisterRequest(EXISTING_USERNAME, EXISTING_EMAIL, VALID_PASSWORD));
    }


    /**
     * =============== POSITIVE TEST CASES
     */
    @DisplayName("test registration with valid credentials - ANV-02-F-1")
    @Test
    void testSuccessfulRegistrationWithValidCredentials() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @DisplayName("test registration with uppercase email normalization - ANV-02-F-2")
    @Test
    void testSuccessfulRegistrationWithUppercaseEmail() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, UPPERCASE_EMAIL, VALID_PASSWORD);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @DisplayName("test registration with email containing whitespace - ANV-02-F-3")
    @Test
    void testSuccessfulRegistrationWithEmailWhitespace() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, EMAIL_WITH_WHITESPACE, VALID_PASSWORD);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @DisplayName("test registration with username containing whitespace - ANV-02-F-4")
    @Test
    void testSuccessfulRegistrationWithUsernameWhitespace() {
        RegisterRequest request = new RegisterRequest(USERNAME_WITH_WHITESPACE, VALID_EMAIL, VALID_PASSWORD);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @DisplayName("test registration with all fields containing whitespace - ANV-02-F-5")
    @Test
    void testSuccessfulRegistrationWithAllFieldsWhitespace() {
        RegisterRequest request = new RegisterRequest(
                USERNAME_WITH_WHITESPACE,
                EMAIL_WITH_WHITESPACE,
                VALID_PASSWORD
        );

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @DisplayName("test registration with minimum valid password - ANV-02-F-6")
    @Test
    void testSuccessfulRegistrationWithMinimumValidPassword() {
        // Password with exactly 8 chars, 1 upper, 1 lower, 2 digits, 1 special
        String minPassword = "AaBbCcEe12!";
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, minPassword);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @DisplayName("test registration with minimum valid username - ANV-02-F-7")
    @Test
    void testSuccessfulRegistrationWithMinimumValidUsername() {
        // Username with exactly 3 characters
        String minUsername = "abc";
        RegisterRequest request = new RegisterRequest(minUsername, VALID_EMAIL, VALID_PASSWORD);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    /**
     * ======= NEGATIVE TEST CASES - DUPLICATE TEST CASES
     */

    @DisplayName("test registration with duplicate email - ANV-02-F-8")
    @Test
    void testFailedRegistrationWithDuplicateEmail() {
        RegisterRequest request = new RegisterRequest("newuser", EXISTING_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with duplicate username - ANV-02-F-9")
    @Test
    void testFailedRegistrationWithDuplicateUsername() {
        RegisterRequest request = new RegisterRequest(EXISTING_USERNAME, "new.email@test.com", VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    /**
     * ====== NEGATIVE TEST CASES - EMPTY VALUE TEST CASES
     */

    @DisplayName("test registration with empty username - ANV-02-F-10")
    @Test
    void testFailedRegistrationWithEmptyUsername() {
        RegisterRequest request = new RegisterRequest(EMPTY_VALUE, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with empty email - ANV-02-F-11")
    @Test
    void testFailedRegistrationWithEmptyEmail() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, EMPTY_VALUE, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with empty password - ANV-02-F-12")
    @Test
    void testFailedRegistrationWithEmptyPassword() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, EMPTY_VALUE);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    /**
     * ====== NEGATIVE TEST CASES - NULL VALUE TEST CASES
     */

    @DisplayName("test registration with null username - ANV-02-F-13")
    @Test
    void testFailedRegistrationWithNullUsername() {
        RegisterRequest request = new RegisterRequest(null, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with null email - ANV-02-F-14")
    @Test
    void testFailedRegistrationWithNullEmail() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, null, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with null password - ANV-02-F-15")
    @Test
    void testFailedRegistrationWithNullPassword() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, null);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with null object - ANV-02-F-16")
    @Test
    void testFailedRegistrationWithNullObject() {
        assertThrows(Exception.class, () -> authService.register(null));
    }

    /**
     * ====== VALIDATION TEST CASES
     */

    @DisplayName("test registration with invalid email format - ANV-02-F-17")
    @Test
    void testFailedRegistrationWithInvalidEmailFormat() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, INVALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with too short username - ANV-02-F-18")
    @Test
    void testFailedRegistrationWithShortUsername() {
        RegisterRequest request = new RegisterRequest(SHORT_USERNAME, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with too short password - ANV-02-F-19")
    @Test
    void testFailedRegistrationWithShortPassword() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, SHORT_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    /**
     * ==================== PASSWORD STRENGTH TEST CASES
     */
    @DisplayName("test registration with password missing uppercase - ANV-02-F-20")
    @Test
    void testFailedRegistrationWithPasswordNoUppercase() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, WEAK_PASSWORD_NO_UPPERCASE);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with password missing lowercase - ANV-02-F-21")
    @Test
    void testFailedRegistrationWithPasswordNoLowercase() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, WEAK_PASSWORD_NO_LOWERCASE);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with password missing digits - ANV-02-F-22")
    @Test
    void testFailedRegistrationWithPasswordNoDigit() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, WEAK_PASSWORD_NO_DIGIT);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with password missing special character - ANV-02-F-23")
    @Test
    void testFailedRegistrationWithPasswordNoSpecialChar() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, WEAK_PASSWORD_NO_SPECIAL);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with password having only one digit - ANV-02-F-24")
    @Test
    void testFailedRegistrationWithPasswordOneDigit() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, WEAK_PASSWORD_ONE_DIGIT);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    /**
     * ==== SQL INJECTION TEST CASES
     */

    @DisplayName("test registration with SQL injection in username - ANV-02-F-25")
    @Test
    void testFailedRegistrationWithSqlInjectionUsername() {
        RegisterRequest request = new RegisterRequest(SQL_INJECTION, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with SQL injection in email - ANV-02-F-26")
    @Test
    void testFailedRegistrationWithSqlInjectionEmail() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, SQL_INJECTION, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    /**
     * ===== EDGE TEST CASES - EXTREME LENGTH
     */

    @DisplayName("test registration with extremely long email - ANV-02-F-27")
    @Test
    void testFailedRegistrationWithExtremelyLongEmail() {
        String longEmail = "a".repeat(256) + "@test.com";
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, longEmail, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with extremely long password - ANV-02-F-28")
    @Test
    void testFailedRegistrationWithExtremelyLongPassword() {
        String longPassword = "A".repeat(1000) + "a1!";
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, longPassword);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with extremely long username - ANV-02-F-29")
    @Test
    void testFailedRegistrationWithExtremelyLongUsername() {
        String longUsername = "a".repeat(500);
        RegisterRequest request = new RegisterRequest(longUsername, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    /**
     * ============== EDGE TEST CASES - WHITE SPACE ONLY
     */

    @DisplayName("test registration with email containing only whitespace - ANV-02-F-30")
    @Test
    void testFailedRegistrationWithWhitespaceOnlyEmail() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, WHITE_SPACE, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with password containing only whitespace - ANV-02-F-31")
    @Test
    void testFailedRegistrationWithWhitespaceOnlyPassword() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, WHITE_SPACE);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @DisplayName("test registration with username containing only whitespace - ANV-02-F-32")
    @Test
    void testFailedRegistrationWithWhitespaceOnlyUsername() {
        RegisterRequest request = new RegisterRequest(WHITE_SPACE, VALID_EMAIL, VALID_PASSWORD);

        assertThrows(Exception.class, () -> authService.register(request));
    }
    }