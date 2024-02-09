package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticleRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.exceptions.ArticleNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.EmptyFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.UniqueNameViolationException;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.function.Predicate;

@Service
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final ValidatorService validatorService;

  @Autowired
  public ArticleServiceImpl(ArticleRepository articleRepository, ValidatorService validatorService) {
    this.articleRepository = articleRepository;
    this.validatorService = validatorService;
  }

  @Override
  public ArticlesDTO generateArticlesDTO() {
    List<Article> listNotDeleted = filterDeletedArticles(articleRepository.findAll());
    return new ArticlesDTO(listNotDeleted);
  }

  @Override
  public ArticlesDTO searchArticles(String search) {
    List<Article> matchingArticles = articleRepository.findByTitleContainingOrContentContaining(search, search);
    List<Article> listNotDeleted = filterDeletedArticles(matchingArticles);
    return new ArticlesDTO(listNotDeleted);
  }

  private List<Article> filterDeletedArticles(List<Article> articles) {
    return articles.stream()
        .filter(article -> !article.isDeleted())
        .collect(Collectors.toList());
  }

  @Override
  public Article addNews(ArticleRequestDTO articleRequest) {
    if (articleRequest == null || articleRequest.getContent() == null || articleRequest.getContent().isEmpty()
        || articleRequest.getTitle() == null || articleRequest.getTitle().isEmpty()) {
      throw new IllegalArgumentException("Title or content are required");
    }
    Optional<Article> existingArticle = articleRepository.findByTitle(articleRequest.getTitle());
    if (existingArticle.isPresent() && !existingArticle.get().isDeleted()) {
      throw new EntityExistsException("News title already exists");
    }
    Article newArticle = new Article(articleRequest.getTitle(), articleRequest.getContent());
    return articleRepository.save(newArticle);
  }

  @Override
  public Article deleteNewsById(Long articleId) {
    Article articleToDelete = articleRepository.findById(articleId).orElseThrow(() -> new EntityNotFoundException("The article is not found"));
    articleToDelete.setDeleted(true);
    articleRepository.save(articleToDelete);
    return articleToDelete;
  }

  public Article editNews(Long newsId, ArticleRequestDTO requestDTO) {
    validateArticleRequestDTO(requestDTO).ifPresent(message -> {
      throw new EmptyFieldsException(message);
    });
    Article articleToEdit = articleRepository.findById(newsId)
        .orElseThrow(() -> {
          throw new ArticleNotFoundException("Article does not exist.");
        });
    if (!articleToEdit.getTitle().equals(requestDTO.getTitle())
        && articleRepository.existsByTitle(requestDTO.getTitle())) {
      throw new UniqueNameViolationException("News title already exists.");
    }

    articleToEdit.setTitle(requestDTO.getTitle());
    articleToEdit.setContent(requestDTO.getContent());
    return articleRepository.save(articleToEdit);
  }

  private Optional<String> validateArticleRequestDTO(ArticleRequestDTO requestDTO) {
    List<String> missingFields = new ArrayList<>();
    validatorService.validateField("title", requestDTO::getTitle, Predicate.not(String::isBlank)).ifPresent(missingFields::add);
    validatorService.validateField("content", requestDTO::getContent, Predicate.not(String::isBlank)).ifPresent(missingFields::add);
    return validatorService.getErrorMessageByMissingFields(missingFields);
  }
}