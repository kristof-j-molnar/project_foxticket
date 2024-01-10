package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArticleServiceImplTest {

  @Test
  void generateArticlesDTO_ReturnsAllArticlesInDTO() {
    var repo = Mockito.mock(ArticleRepository.class);
    Mockito.when(repo.findAll()).thenReturn(
        List.of(new Article("test article", "this is an amazing test article."))
    );

    ArticleServiceImpl articleService = new ArticleServiceImpl(repo);
    ArticlesDTO articlesDTO = new ArticlesDTO(List.of(new Article("test article", "this is an amazing test article.")));

    ArticlesDTO actualArticlesDTO = articleService.generateArticlesDTO();
    assertEquals(articlesDTO.getArticles().get(0).getTitle(), actualArticlesDTO.getArticles().get(0).getTitle());
  }

  @Test
  void searchArticles_withMatchingSearchParameter_givesBackExpectedResult() {
    String search = "brand";
    var repo = Mockito.mock(ArticleRepository.class);
    Mockito.when(repo.findByTitleContainingOrContentContaining(search, search)).thenReturn(
        List.of(new Article("First Article", "This is our brand new article"))
    );

    ArticleServiceImpl articleService = new ArticleServiceImpl(repo);
    ArticlesDTO articlesDTO = new ArticlesDTO(List.of(new Article("First Article", "This is our brand new article"),
        new Article("Second Article", "This is our awesome second article")));

    ArticlesDTO actualArticlesDTO = articleService.searchArticles(search);
    assertEquals(articlesDTO.getArticles().get(0).getTitle(), actualArticlesDTO.getArticles().get(0).getTitle());
  }

  @Test
  void searchArticles_withNonMatchingSearchParameter_returnsEmptyArticlesDTO() {
    String search = "nonMatching";
    var repo = Mockito.mock(ArticleRepository.class);
    Mockito.when(repo.findByTitleContainingOrContentContaining(search, search)).thenReturn(
        new ArrayList<>()
    );

    ArticleServiceImpl articleService = new ArticleServiceImpl(repo);
    ArticlesDTO articlesDTO = new ArticlesDTO(List.of(new Article("test article", "this is an amazing test article.")));

    ArticlesDTO actualArticlesDTO = articleService.searchArticles(search);
    assertTrue(actualArticlesDTO.getArticles().isEmpty());
  }
}