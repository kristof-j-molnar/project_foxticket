package com.greenfoxacademy.springwebapp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {
<<<<<<< HEAD

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void getArticles_withSearchParam_returnsFilteredArticles() throws Exception {
    mockMvc.perform(get("/api/news").param("search", "awesome"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['articles']").exists())
        .andExpect(jsonPath("$['articles']").value(hasSize(1)))
        .andExpect(jsonPath("$['articles'][0]['id']").value(2));
  }

  @Test
  public void getArticles_withoutSearchParam_returnsAllArticles() throws Exception {
    mockMvc.perform(get("/api/news"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['articles']").exists())
        .andExpect(jsonPath("$['articles']").value(hasSize(2)));
  }

  @Test
  public void getArticles_withEmptySearchResult_returns404() throws Exception {
    mockMvc.perform(get("/api/news").param("search", "nonexistent"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("No articles found with the given search criteria."));
  }

  @Test
  public void getArticles_withEmptyStringParam_returnsAllArticles() throws Exception {
    mockMvc.perform(get("/api/news").param("search", ""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['articles']").exists())
        .andExpect(jsonPath("$['articles']").value(hasSize(2)));
  }
=======
    @Autowired
    MockMvc mvc;
    @Test
    public void getAllArticles_returnsArticleDTO () throws Exception {
        mvc.perform(get("/api/news"))
                .andExpect(status().is(200))
                .andExpect( jsonPath("$['articles']").value(hasSize(2)))
                .andExpect(jsonPath("$['articles'][0]['id']").value(1));
    }
>>>>>>> 1e4d9b8 (add all changes requested by reviewers)
}