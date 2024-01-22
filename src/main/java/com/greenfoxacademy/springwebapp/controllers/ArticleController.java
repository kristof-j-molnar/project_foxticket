package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ArticleAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.services.ArticleServiceImpl;

import com.greenfoxacademy.springwebapp.services.UserAuthenticationService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/news")
public class ArticleController {

  private ArticleServiceImpl articleService;
  private UserAuthenticationService userAuthenticationService;

  @Autowired
  public ArticleController(ArticleServiceImpl articleService, UserAuthenticationService userAuthenticationService) {
    this.articleService = articleService;
    this.userAuthenticationService = userAuthenticationService;
  }

  @GetMapping
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

  @PostMapping
  public ResponseEntity<?> addNews(@RequestBody(required = false) ArticleAddingRequestDTO article, Authentication authentication) {
    if (!userAuthenticationService.hasRole("Admin", authentication)) {
      return ResponseEntity.status(403).body(new ErrorMessageDTO("Unauthorized access"));
    }
    try {
      return ResponseEntity.status(200).body(articleService.addNews(article));
    } catch (IllegalArgumentException | EntityExistsException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}