package com.example.myhappyplants;

import com.example.myhappyplants.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SessionControllerIntegrationTest extends IntegrationTest {
    private static final String MIN_REQ_PASSWORD = "aBcDe12!";

    @Test
    @DisplayName("TF-03-F-6")
    void test_register_minimal_request() throws Exception {
        RegisterRequest request = new RegisterRequest("John Doe","test@gmail.com", MIN_REQ_PASSWORD);

        mockMvc.perform(
                postJson("/api/auth/register", request))
                .andExpect(status().isOk()
                );
    }
}