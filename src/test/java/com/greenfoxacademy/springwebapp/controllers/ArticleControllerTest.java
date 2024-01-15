package com.greenfoxacademy.springwebapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.UserLoginDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class ArticleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void getArticles_withSearchParam_returnsFilteredArticles() throws Exception {
    String jwt = login();
    mockMvc.perform(get("/api/news").header("Authorization", "Bearer " + jwt).param("search", "awesome"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['articles']").exists())
        .andExpect(jsonPath("$['articles']").value(hasSize(1)))
        .andExpect(jsonPath("$['articles'][0]['id']").value(1));
  }

  @Test
  public void getArticles_withoutSearchParam_returnsAllArticles() throws Exception {
    String jwt = login();
    mockMvc.perform(get("/api/news").header("Authorization", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['articles']").exists())
        .andExpect(jsonPath("$['articles']").value(hasSize(2)));
  }

  @Test
  public void getArticles_withEmptySearchResult_returns404() throws Exception {
    String jwt = login();
    mockMvc.perform(get("/api/news").header("Authorization", "Bearer " + jwt).param("search", "nonexistent"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$['error']").value("No articles found with the given search criteria."));
  }

  @Test
  public void getArticles_withEmptyStringParam_returnsAllArticles() throws Exception {
    String jwt = login();
    mockMvc.perform(get("/api/news").header("Authorization", "Bearer " + jwt).param("search", ""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['articles']").exists())
        .andExpect(jsonPath("$['articles']").value(hasSize(2)));
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