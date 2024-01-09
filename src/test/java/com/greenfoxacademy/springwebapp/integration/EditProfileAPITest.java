package com.greenfoxacademy.springwebapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EditProfileAPITest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserService userService;

  private ObjectMapper mapper = new ObjectMapper();

  /*@Test
  void checkUserValidity_WithOnlyEmptyFields_ShouldReturnErrorMessage() throws Exception {
    User user = new User("", "", "", "USER");

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Name, password and email are required."));
  }

   */
  @Test
  void checkUserValidity_WithOnlyEmptyInputFields_ShouldReturnErrorMessage() throws Exception {
    String email = "ferenczy.reka01@gmail.com";
    String password = "reka12345";

    String body =  "{\"email\": " + "\"" + email + "\"" + ", \"password\": "
        + "\"" + password + "\"" + "}";

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andReturn();

    String response = result.getResponse().getContentAsString();
    response = response.replace("{\"jwt\":\"", "");
    String token = response.replace("\"}", "");

    User user = new User("", "", "", "USER");

    mockMvc.perform(MockMvcRequestBuilders.patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Name, password and email are required."));
  }

  /*@Test
  void checkUserValidity_WithEmptyPasswordAndNameFields_ShouldReturnErrorMessage() throws Exception {
    User user = new User("", "ferenczy.reka@gmail.com", "", "USER");

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Name and password are required."));
  }

   */
  @Test
  void checkUserValidity_WithEmptyPasswordAndNameFields_ShouldReturnErrorMessage() throws Exception {
    String email = "ferenczy.reka01@gmail.com";
    String password = "reka12345";

    String body =  "{\"email\": " + "\"" + email + "\"" + ", \"password\": "
        + "\"" + password + "\"" + "}";

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andReturn();

    String response = result.getResponse().getContentAsString();
    response = response.replace("{\"jwt\":\"", "");
    String token = response.replace("\"}", "");

    User user = new User("", "ferenczy.reka01@gmail.com", "", "USER");

    mockMvc.perform(MockMvcRequestBuilders.patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Name and password are required."));
  }

  @Test
  void checkUserValidity_WithEmptyNameAndEmailFields_ShouldReturnErrorMessage() throws Exception {
    User user = new User("", "", "reka12345", "USER");

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Name and email are required."));
  }

  @Test
  void checkUserValidity_WithEmptyEmailAndPasswordFields_ShouldReturnErrorMessage() throws Exception {
    User user = new User("reka", "", "", "USER");

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Email and password are required."));
  }

  @Test
  void checkUserValidity_WithEmptyPasswordField_ShouldReturnErrorMessage() throws Exception {
    User user = new User("reka", "ferenczy.reka@gmail.com", "", "USER");

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Password is required."));
  }

  @Test
  void checkUserValidity_WithEmptyNameField_ShouldReturnErrorMessage() throws Exception {
    User user = new User("", "ferenczy.reka@gmail.com", "reka12345", "USER");

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Name is required."));
  }

  @Test
  void checkUserValidity_WithEmptyEmailField_ShouldReturnErrorMessage() throws Exception {
    User user = new User("reka", "", "reka12345", "USER");

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Email is required."));
  }

  @Test
  void checkUserValidity_WithAlreadyUsedEmailField_ShouldReturnErrorMessage() throws Exception {
    User user = new User("reka", "ferenczy.reka01@gmail.com", "reka12345", "USER");

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Email is already taken."));
  }

  @Test
  void checkUserValidity_WithPasswordLessThan8Characters_ShouldReturnErrorMessage() throws Exception {
    User user = new User("reka", "ferenczy.reka01@gmail.com", "reka12345", "USER");

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Password must be at least 8 characters."));
  }

  @Test
  void checkUserValidity_WhenAllInputFieldsAreCorrect_ShouldReturnJSONObject() throws Exception {
    User user = new User("reka", "ferenczy.reka02@gmail.com", "reka12345", "USER");

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(user)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['email']").value("ferenczy.reka02@gmail.com"))
        .andExpect(jsonPath("$['name']").value("reka"));
  }
}

