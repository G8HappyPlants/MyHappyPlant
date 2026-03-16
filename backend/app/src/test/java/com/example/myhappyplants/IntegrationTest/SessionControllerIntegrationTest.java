package com.example.myhappyplants.IntegrationTest;

import com.example.myhappyplants.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SessionControllerIntegrationTest extends IntegrationTest {
	private static final String DUMMY_USERNAME_A = "John Doe";
	private static final String DUMMY_USERNAME_B = "Jane Doe";

	private static final String DUMMY_EMAIL = "test.mail@example.com";

	private static final String MIN_LEN_REQ_PASSWORD = "aBcDeF123!";
	private static final String PASSWORD_MISSING_SPEC_CHAR = "aBcDeF123";
	private static final String PASSWORD_MISSING_LOWERCASE_CHAR = "ABCDEF123!";
	private static final String PASSWORD_MISSING_UPPERCASE_CHAR = "abcdef123!";
	private static final String PASSWORD_MISSING_NUMBER_CHAR = "aBcDeF1!";
	private static final String TOO_SHORT_PASSWORD = "BDeF12!";
	private static final String MAX_LEN_REQ_PASSWORD = "Ab3!cD4@Ef5#Gh6$Ij7%Kl8&Mn9*Op0!Qr1@St2#Uv3$Wx4%Yz5&Ab6*Cd7";
	private static final String TOO_LONG_PASSWORD = "Ab3!cD4@Ef5#Gh6$Ij7%Kl8&Mn9*Op0!Qr1@St2#Uv3$Wx4%Yz5&Ab6*Cd7a";

	@Test
	@DisplayName("TF-03-F-19 - Too short password")
	void test_register_too_short_password() throws Exception {
		RegisterRequest request = new RegisterRequest("TooShortPassword", DUMMY_EMAIL, TOO_SHORT_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("TF-03-F-6 - Missing special character")
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
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("TF-03-F-28 - Success")
	void test_register_maximum_len_requirement_password() throws Exception {
		RegisterRequest request = new RegisterRequest("MaximumLengthPass", DUMMY_EMAIL, MAX_LEN_REQ_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("TF-03-F-28 - Too long password")
	void test_register_too_long_password() throws Exception {
		RegisterRequest request = new RegisterRequest("TooLongPassword", DUMMY_EMAIL, TOO_LONG_PASSWORD);

		mockMvc.perform(postJson("/api/auth/register", request))
				.andExpect(status().isBadRequest());
	}


}