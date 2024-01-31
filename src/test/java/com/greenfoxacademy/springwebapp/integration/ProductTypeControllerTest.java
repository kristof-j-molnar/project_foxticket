package com.greenfoxacademy.springwebapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.ProductTypeAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.UserLoginDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ProductTypeControllerTest {

  ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void addProductType_withRequestBody_returnResponseAnd200() throws Exception {
    ProductTypeAddingRequestDTO request = new ProductTypeAddingRequestDTO("Test type");
    String jwt = login();
    mockMvc.perform(post("/api/product-type").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['id']").value(3))
        .andExpect(jsonPath("$['name']").value("Test type"));
  }

  @Test
  public void addProductType_withEmptyRequestBodyName_returnErrorAnd404() throws Exception {
    ProductTypeAddingRequestDTO request = new ProductTypeAddingRequestDTO("");
    String jwt = login();
    mockMvc.perform(post("/api/product-type").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Name is required"));
  }

  @Test
  public void addProductType_withEmptyRequestBodyNullName_returnErrorAnd404() throws Exception {
    ProductTypeAddingRequestDTO request = new ProductTypeAddingRequestDTO(null);
    String jwt = login();
    mockMvc.perform(post("/api/product-type").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Name is required"));
  }

  @Test
  public void addProductType_withEmptyRequestBody_returnErrorAnd404() throws Exception {
    ProductTypeAddingRequestDTO request = new ProductTypeAddingRequestDTO();
    String jwt = login();
    mockMvc.perform(post("/api/product-type").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Name is required"));
  }

  @Test
  public void addNews_withExistName_returnArticleAnd200() throws Exception {
    ProductTypeAddingRequestDTO request = new ProductTypeAddingRequestDTO("pass");
    String jwt = login();
    mockMvc.perform(post("/api/product-type").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Product type name already exists"));
  }

  private String login() throws Exception {
    UserLoginDTO user = new UserLoginDTO("admin@admin.hu", "adminadmin");
    String responseContent = mockMvc.perform(post("/api/users/login")
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
