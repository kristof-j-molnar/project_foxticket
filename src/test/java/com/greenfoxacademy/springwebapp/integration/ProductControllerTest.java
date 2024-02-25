package com.greenfoxacademy.springwebapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditResponseDTO;
import com.greenfoxacademy.springwebapp.dtos.UserLoginDTO;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import jakarta.transaction.Transactional;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductControllerTest {

  @Autowired
  MockMvc mvc;
  ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  ProductRepository repo;

  @Test
  void getAvailableProducts_ReturnAListAnd200() throws Exception {
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));

    mvc.perform(get("/api/products").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$['products']").value(hasSize(3)))
        .andExpect(jsonPath("$['products'][0]['id']").value(1))
        .andExpect(jsonPath("$['products'][1]['id']").value(2));
  }

  @Test
  void editProduct_ByNotAdminUser_Return403() throws Exception {
    String jwt = login(new UserLoginDTO("user@user.hu", "useruser"));

    mvc.perform(post("/api/products/1").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(403));
  }

  @Test
  void deleteProduct_returnsStatus200() throws Exception {
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));

    mvc.perform(post("/api/products/1").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200));
  }

  @Test
  void deleteProduct_throwsException_AndReturnStatus404() throws Exception {
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));

    mvc.perform(post("/api/products/6").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("The product does not exist!"));
  }

  @Test
  void editProduct_WithRequestDTOWithBlankName_ReturnErrorMessageDTO() throws Exception {
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));
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
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));
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
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));
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
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", 1400, "168 hours",
        "Use this pass for a whole week!", null);

    mvc.perform(patch("/api/products/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Type id is required."));
  }

  @Test
  void editProduct_WithRequestDTOWithBlankNameNullDescriptionAndNullTypeId_ReturnErrorMessageDTOWithCorrectMessage() throws Exception {
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("    ", 1400, "168 hours",
        null, null);

    mvc.perform(patch("/api/products/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Name, Description and Type id are required."));
  }

  @Test
  void editProduct_WithInvalidPathVariable_Return404() throws Exception {
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", 1400, "168 hours",
        "Use this pass for a whole week!", 1L);

    mvc.perform(patch("/api/products/0").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(404));
  }

  @Test
  void editProduct_WithExistingProductName_ReturnErrorMessageDTOWithCorrectMessage() throws Exception {
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("test pass 1", 1400, "168 hours",
        "Use this pass for a whole week!", 2L);

    mvc.perform(patch("/api/products/3").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("ProductName already exists."));
  }

  @Test
  void editProduct_WithInvalidProductType_ReturnErrorMessageDTOWithCorrectMessage() throws Exception {
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", 1400, "168 hours",
        "Use this pass for a whole week!", 0L);

    mvc.perform(patch("/api/products/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("ProductType does not exist."));
  }

  @Test
  void editProduct_WithValidInput_Return200AndProductEditResponseDTO() throws Exception {
    String jwt = login(new UserLoginDTO("admin@admin.hu", "adminadmin"));
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", 1400, "168 hours",
        "Use this pass for a whole week!", 1L);
    ProductEditResponseDTO responseDTO = new ProductEditResponseDTO(1L, "1 week pass", 1400, "168 hours",
        "Use this pass for a whole week!", "ticket");

    mvc.perform(patch("/api/products/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(200))
        .andExpect(content().string(objectMapper.writeValueAsString(responseDTO)));
  }

  private String login(UserLoginDTO user) throws Exception {
    String responseContent = mvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().is(200))
        .andReturn()
        .getResponse()
        .getContentAsString();

    Map<String, String> map = objectMapper.readValue(responseContent, Map.class);
    return map.get("token");
  }
}