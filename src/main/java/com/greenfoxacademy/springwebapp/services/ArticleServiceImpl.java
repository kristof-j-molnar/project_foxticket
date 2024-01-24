package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticleRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.exceptions.EmptyFieldsException;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final LogicService logicService;

  @Autowired
  public ArticleServiceImpl(ArticleRepository articleRepository, LogicService logicService) {
    this.articleRepository = articleRepository;
    this.logicService = logicService;
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

  public Article editNews(Long newsId, ArticleRequestDTO requestDTO) throws IllegalAccessException {
    logicService.getErrorMessageByMissingFields(requestDTO).ifPresent(message -> {
      throw new EmptyFieldsException(message);
    });
    return null;
  }
}