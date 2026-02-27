package com.example.myhappyplants.AuthTest;


import com.example.myhappyplants.service.CryptoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LogoutIntegrationTest {

    @MockBean
    private CryptoService cryptoService;

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private String registerUser(String username, String email, String password) throws Exception {
        // TESTHELPER: Registerar en user via riktiga controller/service/repo och returnerar token.
        String json = """
            {"username":"%s","email":"%s","password":"%s"}
        """.formatted(username, email, password);

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        return node.get("token").asText();
    }

    private String login(String email, String password) throws Exception {
        // TESTHELPER: Loggar in och returnerar token från AuthResponse.
        String json = """
            {"email":"%s","password":"%s"}
        """.formatted(email, password);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        return node.get("token").asText();
    }

    private void logout(String token) throws Exception {
        // TESTHELPER: Kör logout genom riktiga endpointen (JWT-filter + @PreAuthorize).
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    private void assertProtectedOk(String token) throws Exception {
        // TESTHELPER: Token ska ge access till skyddad endpoint.
        mockMvc.perform(get("/api/test/protected")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private void assertProtectedDenied(String token) throws Exception {
        // Efter logout kan svaret bli 401 eller 403 beroende på Security setup.
        // Det viktiga är att det INTE är 200.
        int status = mockMvc.perform(get("/api/test/protected")
                        .header("Authorization", "Bearer " + token))
                .andReturn()
                .getResponse()
                .getStatus();

        assertTrue(status == 401 || status == 403,
                "Expected 401 or 403 after logout, but got " + status);
    }

    @DisplayName("test user can login again after logout - ANV-02-F-2")
    @Test
    void testUserCanLoginAgainAfterLogout() throws Exception {
        // TESTAR PÅ RIKTIGT: register -> login -> protected OK -> logout -> token nekas -> login igen -> ny token OK

        String username = "anv02f2_user";
        String email = "anv02f2@test.com";
        String password = "correctPassword123";

        registerUser(username, email, password);

        String token1 = login(email, password);
        assertProtectedOk(token1);

        logout(token1);
        assertProtectedDenied(token1);

        String token2 = login(email, password);
        assertNotNull(token2);
        assertFalse(token2.isBlank());
        assertNotEquals(token1, token2, "Expected a new token after re-login");

        assertProtectedOk(token2);
    }

    @DisplayName("test logout on one device does not affect other sessions - ANV-02-F-3")
    @Test
    void testLogoutDoesNotAffectOtherSessions() throws Exception {
        // TESTAR PÅ RIKTIGT: två tokens samtidigt -> logout ena -> den nekas -> den andra funkar fortfarande

        String username = "anv02f3_user";
        String email = "anv02f3@test.com";
        String password = "correctPassword123";

        registerUser(username, email, password);

        String token1 = login(email, password);
        String token2 = login(email, password);

        assertNotEquals(token1, token2, "Expected different tokens for separate logins");

        assertProtectedOk(token1);
        assertProtectedOk(token2);

        logout(token1);

        assertProtectedDenied(token1);
        assertProtectedOk(token2);
    }

    @DisplayName("test token cannot be used for other operations after logout - ANV-02-F-14")
    @Test
    void testTokenInvalidAfterLogout() throws Exception {
        // TESTAR PÅ RIKTIGT: efter logout ska token inte längre kunna användas på skyddade endpoints

        String username = "anv02f14_user";
        String email = "anv02f14@test.com";
        String password = "correctPassword123";

        registerUser(username, email, password);

        String token = login(email, password);
        assertProtectedOk(token);

        logout(token);
        assertProtectedDenied(token);
    }

}
