package com.greenfoxacademy.springwebapp.dtos;

import com.greenfoxacademy.springwebapp.models.Article;
import java.util.ArrayList;
import java.util.List;

public class ArticlesDTO {
  private List<Article> articles;

  public ArticlesDTO() {
    articles = new ArrayList<>();
  }

  public ArticlesDTO(List<Article> articles) {
    this();
    this.articles = articles;

  }
  
  public List<Article> getArticles() {
    return articles;
  }

  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }
}

