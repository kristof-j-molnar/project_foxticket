package com.greenfoxacademy.springwebapp.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void getAvailableProducts_ReturnAListAnd200() throws Exception {
        mvc.perform(get("/api/products"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$['products']").value(hasSize(2)))
                .andExpect(jsonPath("$['products'][0]['id']").value(1))
                .andExpect(jsonPath("$['products'][1]['id']").value(2));
    }
}