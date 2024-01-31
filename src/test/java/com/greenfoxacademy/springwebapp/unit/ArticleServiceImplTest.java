package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.ArticleAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import com.greenfoxacademy.springwebapp.services.ArticleServiceImpl;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

class ArticleServiceImplTest {

  ArticleService articleService;

  ArticleRepository articleRepository;

  @BeforeEach
  void init() {
    articleRepository = Mockito.mock(ArticleRepository.class);
    articleService = new ArticleServiceImpl(articleRepository);
  }

  @Test
  void generateArticlesDTO_ReturnsAllArticlesInDTO() {
    Mockito.when(articleRepository.findAll()).thenReturn(
        List.of(new Article("test article", "this is an amazing test article."),
            new Article("test article no.2", "this is the test article no.2"),
            new Article("test article no.3", "this is the test article no.3"))
    );

    ArticlesDTO articlesDTO = new ArticlesDTO(List.of(new Article("test article", "this is an amazing test article."),
        new Article("test article no.2", "this is the test article no.2"),
        new Article("test article no.3", "this is the test article no.3"))
    );

    ArticlesDTO actualArticlesDTO = articleService.generateArticlesDTO();
    assertEquals(articlesDTO.getArticles().size(), actualArticlesDTO.getArticles().size());

    List<String> titles = new ArrayList<>();
    for (int i = 0; i < articlesDTO.getArticles().size(); i++) {
      titles.add(articlesDTO.getArticles().get(i).getTitle());
    }
    List<String> actualTitles = new ArrayList<>();
    for (int i = 0; i < actualArticlesDTO.getArticles().size(); i++) {
      actualTitles.add(actualArticlesDTO.getArticles().get(i).getTitle());
    }
    assertTrue(actualTitles.containsAll(titles));
  }

  @Test
  void searchArticles_withMatchingSearchParameter_givesBackExpectedResult() {
    String search = "brand";
    Mockito.when(articleRepository.findByTitleContainingOrContentContaining(search, search)).thenReturn(
        List.of(new Article("First Article", "This is our brand new article"))
    );

    ArticlesDTO articlesDTO = new ArticlesDTO(List.of(new Article("First Article", "This is our brand new article"),
        new Article("Second Article", "This is our awesome second article")));

    ArticlesDTO actualArticlesDTO = articleService.searchArticles(search);
    assertEquals(articlesDTO.getArticles().get(0).getTitle(), actualArticlesDTO.getArticles().get(0).getTitle());
  }

  @Test
  void searchArticles_withNonMatchingSearchParameter_returnsEmptyArticlesDTO() {
    String search = "nonMatching";
    Mockito.when(articleRepository.findByTitleContainingOrContentContaining(search, search)).thenReturn(
        new ArrayList<>()
    );

    ArticlesDTO actualArticlesDTO = articleService.searchArticles(search);
    assertTrue(actualArticlesDTO.getArticles().isEmpty());
  }

  @Test
  void addNews_withRequestBody_returnArticle() {
    Article newArticle = new Article("Blah-blah", "test content for test article");
    Mockito.when(articleRepository.save(any())).thenReturn(newArticle);

    Article actual = articleService.addNews(new ArticleAddingRequestDTO("Blah-blah", "test content for test article"));

    assertEquals(newArticle.getTitle(), actual.getTitle());
  }

  @Test
  void addNews_withExistTitle_returnError() {
    Article a = new Article("Test Title", "ha-ha-ha");
    a.setDeleted(false);
    Mockito.when(articleRepository.findByTitle("Test Title")).thenReturn(Optional.of(a));

    EntityExistsException exception = assertThrows(EntityExistsException.class,
        () -> articleService.addNews(new ArticleAddingRequestDTO("Test Title", "test content for test article")));

    Assertions.assertEquals("News title already exists", exception.getMessage());
  }

  @Test
  void addNews_withEmptyRequestBodyTitle_returnError() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> articleService.addNews(new ArticleAddingRequestDTO(null, "test content for test article")));

    Assertions.assertEquals("Title or content are required", exception.getMessage());
  }

  @Test
  void addNews_withEmptyRequestBodyContent_returnError() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> articleService.addNews(new ArticleAddingRequestDTO("Test", null)));

    Assertions.assertEquals("Title or content are required", exception.getMessage());
  }

  @Test
  void addNews_withEmptyRequestBody_returnError() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> articleService.addNews(null));

    Assertions.assertEquals("Title or content are required", exception.getMessage());
  }

  @Test
  void deleteNewsById_setIsDeleted_ReturnSuccess() {
    Article article = new Article("Blah-blah", "test content for test article");
    Mockito.when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

    Article actual = articleService.deleteNewsById(1L);
    assertTrue(actual.isDeleted());
  }

  @Test
  void deleteNewsById_IsDeletedIsTrue_ReturnSuccess_() {
    Article article = new Article("Blah-blah", "test content for test article");
    article.setDeleted(true);
    Mockito.when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

    Article actual = articleService.deleteNewsById(1L);
    assertTrue(actual.isDeleted());
  }


  @Test
  void deleteNewsById_withWrongID_ThrowException() {
    EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
        () -> articleService.deleteNewsById(10L));

    Assertions.assertEquals("The article is not found", exception.getMessage());
  }
}