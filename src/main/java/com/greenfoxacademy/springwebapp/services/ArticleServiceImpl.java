package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticleAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;

  @Autowired
  public ArticleServiceImpl(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
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
  public Article addNews(ArticleAddingRequestDTO articleRequest) {
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
}