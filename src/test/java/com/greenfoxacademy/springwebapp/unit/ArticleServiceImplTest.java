package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.models.Article;
<<<<<<< HEAD

import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import com.greenfoxacademy.springwebapp.services.ArticleServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
=======
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

>>>>>>> 88a7d35 (modified tests using mockito)

class ArticleServiceImplTest {

  @Test
  void generateArticlesDTO_ReturnsAllArticlesInDTO() {
    var repo = Mockito.mock(ArticleRepository.class);
    Mockito.when(repo.findAll()).thenReturn(
<<<<<<< HEAD
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
=======
        List.of(new Article("test article", "this is an amazing test article."),
            new Article("test article no.2", "this is the test article no.2"),
            new Article("test article no.3", "this is the test article no.3")));

    ArticleServiceImpl articleService = new ArticleServiceImpl(repo);
    ArticlesDTO articlesDTO = new ArticlesDTO();
    articlesDTO.add(new Article("test article", "this is an amazing test article."));
    articlesDTO.add(new Article("test article no.2", "this is the test article no.2"));
    articlesDTO.add(new Article("test article no.3", "this is the test article no.3"));

    ArticlesDTO actualArticlesDTO = articleService.generateArticlesDTO();
    assertEquals(articlesDTO.getArticles().size(), actualArticlesDTO.getArticles().size());
<<<<<<< HEAD
>>>>>>> 88a7d35 (modified tests using mockito)
=======

    List<String> titles = new ArrayList<>();
    for (int i = 0; i < articlesDTO.getArticles().size(); i++) {
      titles.add(articlesDTO.getArticles().get(i).getTitle());
    }
    List<String> actualTitles = new ArrayList<>();
    for (int i = 0; i < actualArticlesDTO.getArticles().size(); i++) {
      actualTitles.add(actualArticlesDTO.getArticles().get(i).getTitle());
    }

    assertTrue(actualTitles.containsAll(titles));
>>>>>>> ca34ea7 (change Localdate.now to Instant.now)
  }
}