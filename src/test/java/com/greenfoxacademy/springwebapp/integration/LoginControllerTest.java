package com.greenfoxacademy.springwebapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.UserLoginDTO;
import com.greenfoxacademy.springwebapp.services.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class LoginControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  MyUserDetailsService userDetailsService;

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void checkJsonValidity_WithMissingFields_ShouldReturnCustomErrorMessage() throws Exception {

    UserLoginDTO userLoginDTO = new UserLoginDTO(null, null);

    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userLoginDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("All fields are required."));
  }

  @Test
  void checkJsonValidity_WithMissingPassword_ShouldReturnCustomErrorMessage() throws Exception {

    UserLoginDTO userLoginDTO = new UserLoginDTO("lacika.hu", null);

    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userLoginDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Password is required."));
  }

  @Test
  void checkJsonValidity_WithMissingEmail_ShouldReturnCustomErrorMessage() throws Exception {

    UserLoginDTO userLoginDTO = new UserLoginDTO(null, "password");

    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userLoginDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("E-mail is required."));
  }

  @Test
  void checkJsonValidity_WithWrongEmail_ShouldReturnCustomErrorMessage() throws Exception {

    UserLoginDTO userLoginDTO = new UserLoginDTO("laci.com", "pass");

    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userLoginDTO)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Email or password is incorrect"));
  }

  @Test
  void checkJsonValidity_WithWrongPassword_ShouldReturnCustomErrorMessage() throws Exception {

    UserLoginDTO userLoginDTO = new UserLoginDTO("lacika.com", "cica");

    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userLoginDTO)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Email or password is incorrect"));
  }

  @Test
  void checkJsonValidity_WithExistingEmailAndPassword_ShouldReturnStatusOkAndTokenDTOWithStatusOkAndGeneratedToken() throws Exception {

    UserLoginDTO userLoginDTO = new UserLoginDTO("admin@admin.hu", "adminadmin");

    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userLoginDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$['status']").value("ok"))
        .andExpect(jsonPath("$['token']").exists());
  }
}