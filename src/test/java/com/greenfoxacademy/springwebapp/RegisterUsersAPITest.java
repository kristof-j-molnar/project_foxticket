package com.greenfoxacademy.springwebapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.UserRequestDTO;
import com.greenfoxacademy.springwebapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterUsersAPITest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    //checkCredentialValidity_WithValidCredentials_ReturnsTrue()

    @Test
    void checkCredentialValidity_WithEmptyCredential_ShouldReturnErrorMessage() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO("", "", "", "USER");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().is(404))
                .andExpectAll(jsonPath("$['error']").value("Name, email and password are required."));
    }

    @Test
    void checkPasswordLength_WithPasswordLessThan8Characters_ShouldReturnCorrectErrorMessage() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO("Julcsi", "jhfkagvf,ah", "hg", "USER");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().is(404))
                .andExpectAll(jsonPath("$['error']").value("Password must be at least 8 characters."));
    }

    @Test
    void checkCorrectEmailValidity_WithAlreadyExistingEmail_ShouldReturnCorrectErrorMessage() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO("Julcsi", "ferenczy.reka01@gmail.com", "hgjhdfjhfcj", "USER");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().is(404))
                .andExpectAll(jsonPath("$['error']").value("Email is already taken."));
    }

    @Test
    void checkNameValidity_WithEmptyName_ShouldReturnCorrectErrorMessage() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO("", "bdbdbd@gmail.com", "hgjhdfjhfcj", "USER");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().is(404))
                .andExpectAll(jsonPath("$['error']").value("Name is required."));
    }

    @Test
    void checkEmailValidity_WithEmptyEmailInput_ShouldReturnCOrrectErrorMessage() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO("hfcfmjgc", "", "hgjhdfjhfcj", "USER");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().is(404))
                .andExpectAll(jsonPath("$['error']").value("Email is required."));
    }

    @Test
    void checkPasswordValidity_WithEmptyPassword_ShouldReturnCorrectErrorMessage() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO("hfcfmjgc", "hdhjfj@gmail.com", "", "USER");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().is(404))
                .andExpectAll(jsonPath("$['error']").value("Password is required."));
    }

    @Test
    void checkCorrectReturnJSONObject_WhenAllInputFieldsAreCorrect_ReturnsTrue() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO("Pistike", "pistike@gmail.com", "pistike3000", "USER");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().is(200))
                .andExpectAll(jsonPath("$['email']").value("pistike@gmail.com"));
    }
}
