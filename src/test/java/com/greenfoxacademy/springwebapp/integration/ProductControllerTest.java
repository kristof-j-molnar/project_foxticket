package com.greenfoxacademy.springwebapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.UserLoginDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {

  @Autowired
  MockMvc mvc;
  ObjectMapper objectMapper = new ObjectMapper();

  private String login() throws Exception {
    UserLoginDTO user = new UserLoginDTO("admin@admin.hu", "adminadmin");
    String responseContent = mvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andReturn()
        .getResponse()
        .getContentAsString();

    Map<String, String> map = objectMapper.readValue(responseContent, Map.class);
    return map.get("token");
  }

  @Test
  void getAvailableProducts_ReturnAListAnd200() throws Exception {
    String jwt = login();

    mvc.perform(get("/api/products").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$['products']").value(hasSize(3)))
        .andExpect(jsonPath("$['products'][0]['id']").value(1))
        .andExpect(jsonPath("$['products'][1]['id']").value(2));
  }

  @Test
  void editProduct_WithRequestDTOWithBlankName_ReturnErrorMessageDTO() throws Exception {
    String jwt = login();
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO(" ", 1400, "168 hours",
        "Use this pass for a whole week!", 1L);

    mvc.perform(patch("/api/products/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Name is required."));
  }

  @Test
  void editProduct_WithRequestDTOWithNullName_ReturnErrorMessageDTO() throws Exception {
    String jwt = login();
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO(null, 1400, "168 hours",
        "Use this pass for a whole week!", 1L);

    mvc.perform(patch("/api/products/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Name is required."));
  }

  @Test
  void editProduct_WithRequestDTOWithNullPrice_ReturnErrorMessageDTOWithCorrectMessage() throws Exception {
    String jwt = login();
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", null, "168 hours",
        "Use this pass for a whole week!", 1L);

    mvc.perform(patch("/api/products/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Price is required."));
  }

  @Test
  void editProduct_WithRequestDTOWithNullTypeId_ReturnErrorMessageDTOWithCorrectMessage() throws Exception {
    String jwt = login();
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", 1400, "168 hours",
        "Use this pass for a whole week!", null);

    mvc.perform(patch("/api/products/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Type id is required."));
  }

  @Test
  void editProduct_WithInvalidPathVariable_Return404() throws Exception {
    String jwt = login();
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", 1400, "168 hours",
        "Use this pass for a whole week!", 1L);

    mvc.perform(patch("/api/products/0").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(404));
  }

  @Test
  void editProduct_WithExistingProductName_ReturnErrorMessageDTOWithCorrectMessage() throws Exception {
    String jwt = login();
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("teszt bérlet 2", 1400, "168 hours",
        "Use this pass for a whole week!", 2L);

    mvc.perform(patch("/api/products/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("ProductName already exists."));
  }
}