package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticleRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.models.Article;

public interface ArticleService {

  ArticlesDTO generateArticlesDTO();

  ArticlesDTO searchArticles(String search);

  Article addNews(ArticleRequestDTO articleRequest);

  Article editNews(Long newsId, ArticleRequestDTO requestDTO);
}