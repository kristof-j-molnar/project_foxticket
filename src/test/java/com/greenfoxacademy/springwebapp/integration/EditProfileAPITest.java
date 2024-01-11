package com.greenfoxacademy.springwebapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.EditProfileDTO;
import com.greenfoxacademy.springwebapp.dtos.UserLoginDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class EditProfileAPITest {

  @Autowired
  MockMvc mvc;

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void editProfile_ShouldReturn200AndUserResponseDTO() throws Exception {
    String jwt = login();
    EditProfileDTO editProfileDTO = new EditProfileDTO("gizike01", "gizike01@gmail.com", "gizi123456");

    mvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(editProfileDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$['email']").value("gizike01@gmail.com"))
        .andExpect(jsonPath("$['name']").value("gizike01"));
  }

  @Test
  void editProfile_ShouldReturn400andCorrectErrorMessage_WithNoDTOInputFields() throws Exception {
    String jwt = login();
    EditProfileDTO editProfileDTO = new EditProfileDTO("", "", "");

    mvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(editProfileDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Name, password, or email are required."));
  }

  @Test
  void editProfile_ShouldReturn400andCorrectErrorMessage_WithAlreadyUsedEmail() throws Exception {
    String jwt = login();
    EditProfileDTO editProfileDTO = new EditProfileDTO("", "user@user.hu", "");

    mvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(editProfileDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Email is already taken."));
  }

  @Test
  void editProfile_ShouldReturn400andCorrectErrorMessage_WithWrongPassword() throws Exception {
    String jwt = login();
    EditProfileDTO editProfileDTO = new EditProfileDTO("", "", "pass");

    mvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(editProfileDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Password must be at least 8 characters."));
  }

  private String login() throws Exception {
    UserLoginDTO user = new UserLoginDTO("admin@admin.hu", "adminadmin");
    String responseContent = mvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.status").value("ok"))
        .andExpect(jsonPath("$.token").exists())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Map<String, String> map = objectMapper.readValue(responseContent, Map.class);
    return map.get("token");
  }
}
