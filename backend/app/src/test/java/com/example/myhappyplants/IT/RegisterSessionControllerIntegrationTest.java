package com.example.myhappyplants.IT;

import com.example.myhappyplants.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class RegisterSessionControllerIntegrationTest extends IntegrationTest {
    private static final String VALID_LONG_EMAIL = "a".repeat(50) + "@mail.com";
    private static final String TOO_LONG_EMAIL = "a" + VALID_LONG_EMAIL;

    private static final String VALID_LONG_USERNAME = "User" + "a".repeat(170 - 4);
    private static final String TOO_LONG_USERNAME = "User" + "a".repeat(170 - 4) + "a";

    private static final String DUMMY_EMAIL = "test.mail@example.com";
    private static final String INVALID_EMAIL_FORMAT = "invalid@mail";

    private static final String VALID_SHORT_PASSWORD = "aBcDeF123!";
    private static final String PASSWORD_MISSING_SPEC_CHAR = "aBcDeF123";
    private static final String PASSWORD_MISSING_LOWERCASE_CHAR = "ABCDEF123!";
    private static final String PASSWORD_MISSING_UPPERCASE_CHAR = "abcdef123!";
    private static final String PASSWORD_MISSING_NUMBER_CHAR = "aBcDeF1!";
    private static final String PASSWORD_ABSENT_NUMBER_CHAR = "aBcDeF!";
    private static final String TOO_SHORT_PASSWORD = "BDeF12!";
    private static final String VALID_LONG_PASSWORD = "Ab3!cD4@Ef5#Gh6$Ij7%Kl8&Mn9*Op0!Qr1@St2#Uv3$Wx4%Yz5&Ab6*Cd7";
    private static final String TOO_LONG_PASSWORD = "Ab3!cD4@Ef5#Gh6$Ij7%Kl8&Mn9*Op0!Qr1@St2#Uv3$Wx4%Yz5&Ab6*Cd7a";
    private static final String GENERIC_PASSWORD = "abC!!123!!Def";

    // F-6
    @Test
    @DisplayName("TF-03-F-06")
    void test_register_minimum_len_requirement_password() throws Exception {
        RegisterRequest request = new RegisterRequest("MinimumLengthPass", "min.len@password.com", VALID_SHORT_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.token").exists());
    }

    // F-7
    @Test
    @DisplayName("TF-03-F-07")
    void test_register_min_len_username_email() throws Exception {
        RegisterRequest request = new RegisterRequest("suc", DUMMY_EMAIL, GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isNoContent());
    }

    // F-8
    @Test
    @DisplayName("TF-03-F-08")
    void test_register_duplicate_email() throws Exception {
        String duplicateEmail = "duplicate@email.com";

        RegisterRequest request = new RegisterRequest("Username1", duplicateEmail, GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isNoContent());

        request = new RegisterRequest("Username2", duplicateEmail, GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isConflict());
    }

    // Not used? needs discussion
    // // F-9
    // @Test
    // @DisplayName("TF-03-F-09")
    // void test_register_duplicate_username() throws Exception {
    // 	RegisterRequest request = new RegisterRequest("DuplicateUser", "user1@email.com", GENERIC_PASSWORD);
//
    // 	mockMvc.perform(postJson("/api/auth/register", request))
    // 			.andExpect(status().isOk());
//
    // 	request = new RegisterRequest("DuplicateUser", "user2@email.com", GENERIC_PASSWORD);
//
    // 	mockMvc.perform(postJson("/api/auth/register", request))
    // 			.andExpect(status().isConflict());
    // }

    // F-10
    @Test
    @DisplayName("TF-03-F-10")
    void test_register_blank_username() throws Exception {
        RegisterRequest request = new RegisterRequest("", DUMMY_EMAIL, GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-11
    @Test
    @DisplayName("TF-03-F-11")
    void test_register_blank_email() throws Exception {
        RegisterRequest request = new RegisterRequest("BlankEmail", "", GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-12
    @Test
    @DisplayName("TF-03-F-12")
    void test_register_blank_password() throws Exception {
        RegisterRequest request = new RegisterRequest("BlankPassword", DUMMY_EMAIL, "");

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-13
    @Test
    @DisplayName("TF-03-F-13")
    void test_register_null_username() throws Exception {
        RegisterRequest request = new RegisterRequest(null, DUMMY_EMAIL, "");

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-14
    @Test
    @DisplayName("TF-03-F-14")
    void test_register_null_email() throws Exception {
        RegisterRequest request = new RegisterRequest("NullEmail", null, "");

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-15
    @Test
    @DisplayName("TF-03-F-15")
    void test_register_null_password() throws Exception {
        RegisterRequest request = new RegisterRequest("NullEmail", DUMMY_EMAIL, null);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-16
    @Test
    @DisplayName("TF-03-F-16")
    void test_register_null_request() throws Exception {
        RegisterRequest request = null;

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-17
    @Test
    @DisplayName("TF-03-F-17")
    void test_register_invalid_email() throws Exception {
        RegisterRequest request = new RegisterRequest("InvalidEmailFormat", INVALID_EMAIL_FORMAT, GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-18
    @Test
    @DisplayName("TF-03-F-18")
    void test_register_too_short_username() throws Exception {
        RegisterRequest request = new RegisterRequest("sh", DUMMY_EMAIL, GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-19
    @Test
    @DisplayName("TF-03-F-19")
    void test_register_too_short_password() throws Exception {
        RegisterRequest request = new RegisterRequest("TooShortPassword", DUMMY_EMAIL, TOO_SHORT_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-20
    @Test
    @DisplayName("TF-03-F-20")
    void test_register_missing_uppercase_char() throws Exception {
        RegisterRequest request = new RegisterRequest("MissingUpperCase", DUMMY_EMAIL, PASSWORD_MISSING_UPPERCASE_CHAR);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-21
    @Test
    @DisplayName("TF-03-F-21")
    void test_register_missing_lowercase_char() throws Exception {
        RegisterRequest request = new RegisterRequest("MissingLowerCase", DUMMY_EMAIL, PASSWORD_MISSING_LOWERCASE_CHAR);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-22
    @Test
    @DisplayName("TF-03-F-22")
    void test_register_absent_digit_char() throws Exception {
        RegisterRequest request = new RegisterRequest("MissingNumberCharacter", DUMMY_EMAIL, PASSWORD_ABSENT_NUMBER_CHAR);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-23
    @Test
    @DisplayName("TF-03-F-23")
    void test_register_missing_spec_char() throws Exception {
        RegisterRequest request = new RegisterRequest("MissingSpecialCharacter", DUMMY_EMAIL, PASSWORD_MISSING_SPEC_CHAR);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-24
    @Test
    @DisplayName("TF-03-F-24")
    void test_register_missing_digit_char() throws Exception {
        RegisterRequest request = new RegisterRequest("MissingSingleDigit", DUMMY_EMAIL, PASSWORD_MISSING_NUMBER_CHAR);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-25
    @Test
    @DisplayName("TF-03-F-25")
    void test_register_sql_injection_username() throws Exception {
        RegisterRequest request = new RegisterRequest("' OR 1=1 -- '", DUMMY_EMAIL, GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-26
    @Test
    @DisplayName("TF-03-F-26")
    void test_register_sql_injection_email() throws Exception {
        RegisterRequest request = new RegisterRequest("SqlInjectionAttempt", "' OR 1=1 -- '", GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-27
    @Test
    @DisplayName("TF-03-F-27")
    /**
     * Related: F-33
     */
    void test_register_very_long_email() throws Exception {
        RegisterRequest request = new RegisterRequest("VeryLongEmailValid", VALID_LONG_EMAIL, GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("TF-03-F-28")
    void test_register_valid_long_password() throws Exception {
        RegisterRequest request = new RegisterRequest("TooLongPassword", DUMMY_EMAIL, VALID_LONG_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isNoContent());
    }

    // F-29
    @Test
    @DisplayName("TF-03-F-29")
    void test_register_valid_long_username() throws Exception {
        RegisterRequest request = new RegisterRequest(VALID_LONG_USERNAME, "very.long@email.com", GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isNoContent());
    }

    // F-30
    @Test
    @DisplayName("TF-03-F-30")
    void test_register_whitespace_email() throws Exception {
        RegisterRequest request = new RegisterRequest("WhitespaceEmail", " ", GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-31
    @Test
    @DisplayName("TF-03-F-31")
    void test_register_whitespace_password() throws Exception {
        RegisterRequest request = new RegisterRequest("WhitespacePassword", "whitespace@email.com", " ");

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-32
    @Test
    @DisplayName("TF-03-F-32")
    void test_register_whitespace_username() throws Exception {
        RegisterRequest request = new RegisterRequest(" ", "whitespace.username@email.com", GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    // F-33
    @Test
    @DisplayName("TF-03-F-33")
    /**
     * Related: F-27
     */
    void test_register_too_long_email() throws Exception {
        RegisterRequest request = new RegisterRequest("TooLongEmail", TOO_LONG_EMAIL, GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TF-03-F-34")
    /**
     * Related: F-28
     */
    void test_register_too_long_password() throws Exception {
        RegisterRequest request = new RegisterRequest("TooLongPassword", DUMMY_EMAIL, TOO_LONG_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TF-03-F-35")
    void test_register_too_long_username() throws Exception {
        RegisterRequest request = new RegisterRequest(TOO_LONG_USERNAME, "very.long@email.com", GENERIC_PASSWORD);

        mockMvc.perform(postJson("/api/auth/register", request))
                .andExpect(status().isNoContent());
    }
}