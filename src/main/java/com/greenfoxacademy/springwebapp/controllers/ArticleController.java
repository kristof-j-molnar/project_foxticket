package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ArticleRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticlesDTO;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.exceptions.ArticleNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.EmptyFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.UniqueNameViolationException;
import com.greenfoxacademy.springwebapp.services.ArticleServiceImpl;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class ArticleController {

  private final ArticleServiceImpl articleService;

  @Autowired
  public ArticleController(ArticleServiceImpl articleService) {
    this.articleService = articleService;
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
  public ResponseEntity<?> addNews(@RequestBody(required = false) ArticleRequestDTO article) {
    try {
      return ResponseEntity.status(200).body(articleService.addNews(article));
    } catch (IllegalArgumentException | EntityExistsException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @PutMapping(path = "/{newsId}")
  public ResponseEntity<?> editNews(@PathVariable Long newsId,
                                    @RequestBody(required = false) ArticleRequestDTO articleRequestDTO) {
    try {
      return ResponseEntity.ok(articleService.editNews(newsId, articleRequestDTO));
    } catch (EmptyFieldsException | UniqueNameViolationException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    } catch (ArticleNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}