package com.greenfoxacademy.springwebapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingRequestDTO;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CartControllerTest {

  @Autowired
  MockMvc mvc;

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @Transactional
  void getProductsInCart_ReturnAListAnd200() throws Exception {
    String jwt = login();

    mvc.perform(get("/api/cart").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$['cart']").value(hasSize(3)))
        .andExpect(jsonPath("$['cart'][0]['product_id']").value(1))
        .andExpect(jsonPath("$['cart'][1]['price']").value(4000))
        .andExpect(jsonPath("$['cart'][2]['price']").value(9500));
  }

  @Test
  @Transactional
  void addProductToCart_ReturnResponseDTOAnd200() throws Exception {
    String jwt = login();
    ProductAddingRequestDTO request = new ProductAddingRequestDTO(1L);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$['cartId']").value(2))
        .andExpect(jsonPath("$['productId']").value(1))
        .andExpect(jsonPath("$['amount']").value(2));
  }

  @Test
  @Transactional
  void addNotExistProductToCart_ReturnErrorDTOAnd404() throws Exception {
    String jwt = login();
    ProductAddingRequestDTO request = new ProductAddingRequestDTO(10L);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Product is not found"));
  }

  @Test
  @Transactional
  void addNoProductToCart_ReturnErrorDTOAnd404() throws Exception {
    String jwt = login();
    ProductAddingRequestDTO request = null;

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Product ID is required."));
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