package com.example.myhappyplants.IT;

import com.example.myhappyplants.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RegisterSessionControllerIntegrationTest extends IntegrationTest {
	private static final String DUMMY_USERNAME_A = "John Doe";
	private static final String DUMMY_USERNAME_B = "Jane Doe";

	// 170-chars due to Base64 + IV padding in DB increases data by almost 33% in size
	private static final String VALID_LONG_EMAIL = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@mail.com";
	private static final String TOO_LONG_EMAIL = "a" + VALID_LONG_EMAIL;

	private static final String VALID_LONG_USERNAME = "User" + "a".repeat(255-4-1);
	private static final String TOO_LONG_USERNAME = "User" + "a".repeat(255-4-1) + "a";

	private static final String DUMMY_EMAIL = "test.mail@example.com";
	private static final String INVALID_EMAIL_FORMAT = "invalid@mail";

	private static final String MIN_LEN_REQ_PASSWORD = "aBcDeF123!";
	private static final String PASSWORD_MISSING_SPEC_CHAR = "aBcDeF123";
	private static final String PASSWORD_MISSING_LOWERCASE_CHAR = "ABCDEF123!";
	private static final String PASSWORD_MISSING_UPPERCASE_CHAR = "abcdef123!";
	private static final String PASSWORD_MISSING_NUMBER_CHAR = "aBcDeF1!";
	private static final String TOO_SHORT_PASSWORD = "BDeF12!";
	private static final String MAX_LEN_REQ_PASSWORD = "Ab3!cD4@Ef5#Gh6$Ij7%Kl8&Mn9*Op0!Qr1@St2#Uv3$Wx4%Yz5&Ab6*Cd7";
	private static final String TOO_LONG_PASSWORD = "Ab3!cD4@Ef5#Gh6$Ij7%Kl8&Mn9*Op0!Qr1@St2#Uv3$Wx4%Yz5&Ab6*Cd7a";
	private static final String GENERIC_PASSWORD = "abC!!123!!Def";

	@Test
	@DisplayName("TF-03-F-19 - Too short password")
	void test_register_too_short_password() throws Exception {
		RegisterRequest request = new RegisterRequest("TooShortPassword", DUMMY_EMAIL, TOO_SHORT_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-23 - Missing special character")
	void test_register_missing_spec_char() throws Exception {
		RegisterRequest request = new RegisterRequest("MissingSpecialCharacter", DUMMY_EMAIL, PASSWORD_MISSING_SPEC_CHAR);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-20 - Missing uppercase letter")
	void test_register_missing_uppercase_char() throws Exception {
		RegisterRequest request = new RegisterRequest("MissingUpperCase", DUMMY_EMAIL, PASSWORD_MISSING_UPPERCASE_CHAR);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-21 - Missing lowercase letter")
	void test_register_missing_lowercase_char() throws Exception {
		RegisterRequest request = new RegisterRequest("MissingLowerCase", DUMMY_EMAIL, PASSWORD_MISSING_LOWERCASE_CHAR);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-22	 - Missing number character")
	void test_register_missing_digit_char() throws Exception {
		RegisterRequest request = new RegisterRequest("MissingNumberCharacter", DUMMY_EMAIL, PASSWORD_MISSING_NUMBER_CHAR);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-6 - Success")
	void test_register_minimum_len_requirement_password() throws Exception {
		RegisterRequest request = new RegisterRequest("MinimumLengthPass", DUMMY_EMAIL, MIN_LEN_REQ_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists());
	}

	@Test
	@DisplayName("TF-03-F-28 - Success")
	void test_register_maximum_len_requirement_password() throws Exception {
		RegisterRequest request = new RegisterRequest("MaximumLengthPass", DUMMY_EMAIL, MAX_LEN_REQ_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists());
	}

	@Test
	@DisplayName("TF-03-F-28 - Fail")
	void test_register_too_long_password() throws Exception {
		RegisterRequest request = new RegisterRequest("TooLongPassword", DUMMY_EMAIL, TOO_LONG_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-18")
	void test_register_too_short_username() throws Exception {
		RegisterRequest request = new RegisterRequest("sh", DUMMY_EMAIL, GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register",request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-10")
	void test_register_blank_username() throws Exception {
		RegisterRequest request = new RegisterRequest("", DUMMY_EMAIL, GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-11")
	void test_register_blank_email() throws Exception {
		RegisterRequest request = new RegisterRequest("BlankEmail", "", GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-12")
	void test_register_blank_password() throws Exception {
		RegisterRequest request = new RegisterRequest("BlankPassword", DUMMY_EMAIL, "");

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-13")
	void test_register_null_username() throws Exception {
		RegisterRequest request = new RegisterRequest(null, DUMMY_EMAIL, "");

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-14")
	void test_register_null_email() throws Exception {
		RegisterRequest request = new RegisterRequest("NullEmail", null	, "");

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-15")
	void test_register_null_password() throws Exception {
		RegisterRequest request = new RegisterRequest("NullEmail", DUMMY_EMAIL	, null);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-16")
	void test_register_null_request() throws Exception {
		RegisterRequest request = null;

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-17")
	void test_register_invalid_email() throws Exception {
		RegisterRequest request = new RegisterRequest("InvalidEmailFormat", INVALID_EMAIL_FORMAT, GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-25")
	void test_register_sql_injection_username() throws Exception {
		RegisterRequest request = new RegisterRequest("' OR 1=1 -- '", DUMMY_EMAIL, GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-26")
	void test_register_sql_injection_email() throws Exception {
		RegisterRequest request = new RegisterRequest("SqlInjectionAttempt", "' OR 1=1 -- '", GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-7 - Success")
	void test_register_very_long_email() throws Exception {
		RegisterRequest request = new RegisterRequest("VeryLongEmailValid", VALID_LONG_EMAIL, GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("TF-03-F-7 - Fail")
	void test_register_too_long_email() throws Exception {
		RegisterRequest request = new RegisterRequest("TooLongEmail", TOO_LONG_EMAIL, GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-7 - Success")
	void test_register_min_len_username_email() throws Exception {
		RegisterRequest request = new RegisterRequest("suc", DUMMY_EMAIL, GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("TF-03-F-7 - Fail")
	void test_register_too_short_username_email() throws Exception {
		RegisterRequest request = new RegisterRequest("fl", DUMMY_EMAIL, GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-8")
	void test_register_duplicate_email() throws Exception {
		String duplicateEmail = "duplicate@email.com";

		RegisterRequest request = new RegisterRequest("Username1", duplicateEmail, GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isOk());

		request = new RegisterRequest("Username2", duplicateEmail, GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isConflict());
	}

	@Test
	@DisplayName("TF-03-F-9")
	void test_register_duplicate_username() throws Exception {
		RegisterRequest request = new RegisterRequest("DuplicateUser", "user1@email.com", GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isOk());

		request = new RegisterRequest("DuplicateUser", "user2@email.com", GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isConflict());
	}

	@Test
	@DisplayName("TF-03-F-29 - Success")
	void test_register_max_len_username() throws Exception {
		RegisterRequest request = new RegisterRequest(VALID_LONG_USERNAME, "very.long@email.com", GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("TF-03-F-29 - Fail")
	void test_register_too_long_username() throws Exception {
		RegisterRequest request = new RegisterRequest(TOO_LONG_USERNAME, "very.long@email.com", GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-30")
	void test_register_whitespace_email() throws Exception {
		RegisterRequest request = new RegisterRequest("WhitespaceEmail", " ", GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}
	@Test
	@DisplayName("TF-03-F-31")
	void test_register_whitespace_password() throws Exception {
		RegisterRequest request = new RegisterRequest("WhitespacePassword", "whitespace@email.com", " ");

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}
	@Test
	@DisplayName("TF-03-F-32")
	void test_register_whitespace_username() throws Exception {
		RegisterRequest request = new RegisterRequest(" ", "whitespace.username@email.com", GENERIC_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

}