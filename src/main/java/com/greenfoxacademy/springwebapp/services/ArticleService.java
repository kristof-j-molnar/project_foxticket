package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;

<<<<<<< HEAD
public interface ArticleService {

  ArticlesDTO generateArticlesDTO();
=======
import java.util.List;

@Service
public interface ArticleService {

  public ArticleDTO generateArticleDTO();
>>>>>>> 6af8150 (add styling changes and evironment variables in application.properties)

  ArticlesDTO searchArticles(String search);
}