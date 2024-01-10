package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.services.ArticleServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/news")
public class ArticleController {

  private ArticleServiceImpl articleService;

  @Autowired
  public ArticleController(ArticleServiceImpl articleService) {
    this.articleService = articleService;
  }

  @GetMapping
  public ResponseEntity<ArticlesDTO> getAllArticles() {
    return ResponseEntity.ok(articleService.generateArticlesDTO());
  }

  public ResponseEntity<?> getArticles(@RequestParam(name = "search", required = false) String search) {
    if (search != null && !search.isEmpty()) {
      ArticlesDTO result = articleService.searchArticles(search);
      if (result.getArticles().isEmpty()) {
        return ResponseEntity.status(404).body(new ErrorMessageDTO("No articles found with the given search criteria."));
      } else {
        return ResponseEntity.ok(result);
      }
    } else {
      return ResponseEntity.ok(articleService.generateArticlesDTO());
    }
  }
}