package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.ArticleRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.exceptions.ArticleNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.EmptyFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.UniqueNameViolationException;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import com.greenfoxacademy.springwebapp.services.ArticleServiceImpl;
import com.greenfoxacademy.springwebapp.services.ValidatorService;
import jakarta.persistence.EntityExistsException;
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
  ValidatorService validatorService;

  @BeforeEach
  void init() {
    articleRepository = Mockito.mock(ArticleRepository.class);
    validatorService = Mockito.mock(ValidatorService.class);
    articleService = new ArticleServiceImpl(articleRepository, validatorService);
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

    Article actual = articleService.addNews(new ArticleRequestDTO("Blah-blah", "test content for test article"));

    assertEquals(newArticle.getTitle(), actual.getTitle());
  }

  @Test
  void addNews_withExistTitle_returnError() {
    Mockito.when(articleRepository.existsByTitle("Test Title")).thenReturn(true);

    EntityExistsException exception = assertThrows(EntityExistsException.class,
        () -> articleService.addNews(new ArticleRequestDTO("Test Title", "test content for test article")));

    Assertions.assertEquals("News title already exists", exception.getMessage());
  }

  @Test
  void addNews_withEmptyRequestBodyTitle_returnError() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> articleService.addNews(new ArticleRequestDTO(null, "test content for test article")));

    Assertions.assertEquals("Title or content are required", exception.getMessage());
  }

  @Test
  void addNews_withEmptyRequestBodyContent_returnError() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> articleService.addNews(new ArticleRequestDTO("Test", null)));

    Assertions.assertEquals("Title or content are required", exception.getMessage());
  }

  @Test
  void addNews_withEmptyRequestBody_returnError() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> articleService.addNews(null));

    Assertions.assertEquals("Title or content are required", exception.getMessage());
  }

  @Test
  void editNews_WithEmptyArticleRequestDTO_throwsEmptyFieldsException() {
    ArticleRequestDTO requestDTO = new ArticleRequestDTO();
    Mockito.when(validatorService.validateArticleRequestDTO(requestDTO))
        .thenReturn(Optional.of("Title and content are required."));

    Assertions.assertThrows(EmptyFieldsException.class,
        () -> articleService.editNews(1L, requestDTO));
    Mockito.verifyNoInteractions(articleRepository);
  }

  @Test
  void editNews_WithInvalidNewsId_throwsArticleNotFoundException() {
    ArticleRequestDTO requestDTO = new ArticleRequestDTO("test", "test");
    Mockito.when(validatorService.validateArticleRequestDTO(requestDTO))
        .thenReturn(Optional.empty());
    Mockito.when(articleRepository.findById(0L)).thenReturn(Optional.empty());

    Assertions.assertThrows(ArticleNotFoundException.class,
        () -> articleService.editNews(0L, requestDTO));
  }

  @Test
  void editNews_WithExistingArticleTitle_throwsUniqueNameViolationException() {
    ArticleRequestDTO requestDTO = new ArticleRequestDTO("test", "test");
    Mockito.when(validatorService.validateArticleRequestDTO(requestDTO))
        .thenReturn(Optional.empty());
    Mockito.when(articleRepository.findById(1L)).thenReturn(Optional.of(new Article("test1", "test")));
    Mockito.when(articleRepository.existsByTitle("test")).thenReturn(true);

    Assertions.assertThrows(UniqueNameViolationException.class,
        () -> articleService.editNews(1L, requestDTO));
  }

  @Test
  void editNews_WithValidInput_returnsEditedArticle() {
    Long newsId = 1L;
    ArticleRequestDTO requestDTO = new ArticleRequestDTO("anything", "something");
    Mockito.when(validatorService.validateArticleRequestDTO(requestDTO)).thenReturn(Optional.empty());
    Article articleToEdit = new Article("test1", "test");
    Mockito.when(articleRepository.findById(newsId)).thenReturn(Optional.of(articleToEdit));
    Mockito.when(articleRepository.existsByTitle("anything")).thenReturn(false);
    articleToEdit.setTitle(requestDTO.getTitle());
    articleToEdit.setContent(requestDTO.getContent());
    Mockito.when(articleRepository.save(articleToEdit)).thenReturn(articleToEdit);

    Article article = articleService.editNews(newsId, requestDTO);
    Assertions.assertEquals("anything", article.getTitle());
    Assertions.assertEquals("something", article.getContent());
  }
  //mikor nem hívjuk meg az adott kódrészletet
}