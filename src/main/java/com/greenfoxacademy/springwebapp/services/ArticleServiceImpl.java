package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
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
<<<<<<< HEAD
<<<<<<< HEAD
  public ArticlesDTO generateArticlesDTO() {
    List<Article> articles = articleRepository.findAll();
    return new ArticlesDTO(articles);
  }

  @Override
  public ArticlesDTO searchArticles(String search) {
    List<Article> matchingArticles = articleRepository.findByTitleContainingOrContentContaining(search, search);
    return new ArticlesDTO(matchingArticles);
  }
}
=======
  public ArticleDTO generateArticleDTO() {
=======
  public ArticlesDTO generateArticleDTO() {
>>>>>>> 1e4d9b8 (add all changes requested by reviewers)
    List<Article> articles = articleRepository.findAll();

    return new ArticlesDTO(articles);
  }
}
>>>>>>> 6af8150 (add styling changes and evironment variables in application.properties)
