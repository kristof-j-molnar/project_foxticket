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

@Service
public class ArticleServiceImpl implements ArticleService {

  private ArticleRepository articleRepository;

  @Autowired
  public ArticleServiceImpl(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
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

  public Article addNews(ArticleAddingRequestDTO articleRequest){
    if (articleRequest == null || articleRequest.getContent() == null || articleRequest.getContent().isEmpty() || articleRequest.getTitle() == null || articleRequest.getTitle().isEmpty()) {
      throw new IllegalArgumentException("Title or content are required");
    }
    if (articleRepository.existsArticleByTitle(articleRequest.getTitle())) {
      throw new EntityExistsException("News title already exists");
    }
    Article newArticle = new Article(articleRequest.getTitle(), articleRequest.getContent());
    return articleRepository.save(newArticle);
  }
}