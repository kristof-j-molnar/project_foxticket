package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticleRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.exceptions.ArticleNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.EmptyFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.UniqueNameViolationException;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    List<Article> articles = articleRepository.findAll();
    return new ArticlesDTO(articles);
  }

  @Override
  public ArticlesDTO searchArticles(String search) {
    List<Article> matchingArticles = articleRepository.findByTitleContainingOrContentContaining(search, search);
    return new ArticlesDTO(matchingArticles);
  }

  @Override
  public Article addNews(ArticleRequestDTO articleRequest) {
    if (articleRequest == null || articleRequest.getContent() == null || articleRequest.getContent().isEmpty()
        || articleRequest.getTitle() == null || articleRequest.getTitle().isEmpty()) {
      throw new IllegalArgumentException("Title or content are required");
    }
    if (articleRepository.existsByTitle(articleRequest.getTitle())) {
      throw new EntityExistsException("News title already exists");
    }
    Article newArticle = new Article(articleRequest.getTitle(), articleRequest.getContent());
    return articleRepository.save(newArticle);
  }

  @Override
  public Article editNews(Long newsId, ArticleRequestDTO requestDTO) {
    validatorService.validateArticleRequestDTO(requestDTO).ifPresent(message -> {
      throw new EmptyFieldsException(message);
    });
    Article articleToEdit = articleRepository.findById(newsId)
        .orElseThrow(() -> {
          throw new ArticleNotFoundException("Article does not exist.");
        });
    if (articleToEdit.getTitle() != requestDTO.getTitle()
        && articleRepository.existsByTitle(requestDTO.getTitle())) {
      throw new UniqueNameViolationException("News title already exists.");
    }

    articleToEdit.setTitle(requestDTO.getTitle());
    articleToEdit.setContent(requestDTO.getContent());
    return articleRepository.save(articleToEdit);
  }
}