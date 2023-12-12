package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;

<<<<<<< HEAD
<<<<<<< HEAD
public interface ArticleService {

  ArticlesDTO generateArticlesDTO();
=======
import java.util.List;

@Service
public interface ArticleService {

  public ArticleDTO generateArticleDTO();
>>>>>>> 6af8150 (add styling changes and evironment variables in application.properties)
=======
public interface ArticleService {

  public ArticlesDTO generateArticleDTO();
>>>>>>> 1e4d9b8 (add all changes requested by reviewers)

  ArticlesDTO searchArticles(String search);
}