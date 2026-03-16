package com.example.myhappyplants;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String json(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    protected MockHttpServletRequestBuilder postJson(String urlTemplate, Object content) throws Exception {
        return MockMvcRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(content));
    }
}