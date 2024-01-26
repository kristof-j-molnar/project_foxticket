package com.greenfoxacademy.springwebapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.ArticleRequestDTO;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ArticleControllerTest {

  ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  private MockMvc mockMvc;

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

  @Test
  public void getArticles_withSearchParam_returnsFilteredArticles() throws Exception {
    String jwt = login();
    mockMvc.perform(get("/api/news").header("Authorization", "Bearer " + jwt).param("search", "Lorem"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['articles']").exists())
        .andExpect(jsonPath("$['articles']").value(hasSize(2)))
        .andExpect(jsonPath("$['articles'][0]['id']").value(1));
  }

  @Test
  public void getArticles_withoutSearchParam_returnsAllArticles() throws Exception {
    String jwt = login();
    mockMvc.perform(get("/api/news").header("Authorization", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['articles']").exists())
        .andExpect(jsonPath("$['articles']").value(hasSize(3)));
  }

  @Test
  public void getArticles_withEmptySearchResult_returns404WithCorrectErrorMessage() throws Exception {
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
        .andExpect(jsonPath("$['articles']").value(hasSize(3)));
  }

  @Test
  public void addNews_withRequestBody_returnArticleAnd200() throws Exception {
    ArticleRequestDTO request = new ArticleRequestDTO("Blah-blah", "test content");
    String jwt = login();
    mockMvc.perform(post("/api/news").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['id']").value(4))
        .andExpect(jsonPath("$['title']").value("Blah-blah"));
  }

  @Test
  public void addNews_withEmptyRequestBodyTitle_returnErrorAnd404() throws Exception {
    ArticleRequestDTO request = new ArticleRequestDTO(null, "test content");
    String jwt = login();
    mockMvc.perform(post("/api/news").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Title or content are required"));
  }

  @Test
  public void addNews_withEmptyRequestBodyContent_returnErrorAnd404() throws Exception {
    ArticleRequestDTO request = new ArticleRequestDTO("blah-blah", "");
    String jwt = login();
    mockMvc.perform(post("/api/news").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Title or content are required"));
  }

  @Test
  public void addNews_withEmptyRequestBody_returnErrorAnd404() throws Exception {
    ArticleRequestDTO request = null;
    String jwt = login();
    mockMvc.perform(post("/api/news").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Title or content are required"));
  }

  @Test
  public void addNews_withExistTitle_returnArticleAnd200() throws Exception {
    ArticleRequestDTO request = new ArticleRequestDTO("Test article 1", "Test content");
    String jwt = login();
    mockMvc.perform(post("/api/news").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("News title already exists"));
  }

  @Test
  public void editNews_withEmptyFieldsInRequest_returnErrorDTOWithCorrectMessage() throws Exception {
    String jwt = login();
    ArticleRequestDTO request = new ArticleRequestDTO("", "");
    mockMvc.perform(put("/api/news/2").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Title and content are required."));
  }

  @Test
  public void editNews_withEmptyContentInRequest_returnErrorDTOWithCorrectMessage() throws Exception {
    String jwt = login();
    ArticleRequestDTO request = new ArticleRequestDTO("Test article 1", "");
    mockMvc.perform(put("/api/news/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("Content is required."));
  }

  @Test
  public void editNews_withExistingNewsTitle_returnErrorDTOWithCorrectMessage() throws Exception {
    String jwt = login();
    ArticleRequestDTO request = new ArticleRequestDTO("Test article 2", "some test content for fun");
    mockMvc.perform(put("/api/news/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("News title already exists."));
  }

  @Test
  public void editNews_withInvalidId_returnErrorDTOWithCorrectMessage() throws Exception {
    String jwt = login();
    ArticleRequestDTO request = new ArticleRequestDTO("Test article", "some test content for fun");
    mockMvc.perform(put("/api/news/0").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$['error']").value("Article does not exist."));
  }

  @Test
  public void editNews_withValidInput_return200AndArticle() throws Exception {
    String jwt = login();
    ArticleRequestDTO request = new ArticleRequestDTO("Test article", "some test content for fun");
    mockMvc.perform(put("/api/news/1").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$['id']").value(1))
        .andExpect(jsonPath("$['title']").value("Test article"))
        .andExpect(jsonPath("$['content']").value("some test content for fun"));
  }
}