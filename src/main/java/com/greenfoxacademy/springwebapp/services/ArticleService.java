package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticleAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.models.Article;

public interface ArticleService {

  ArticlesDTO generateArticlesDTO();

  ArticlesDTO searchArticles(String search);

  Article addNews(ArticleAddingRequestDTO articleRequest);

  Article deleteNewsById(Long articleId);
}