package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;

public interface ArticleService {

  ArticlesDTO generateArticlesDTO();

  ArticlesDTO searchArticles(String search);
}