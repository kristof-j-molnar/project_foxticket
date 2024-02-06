package com.greenfoxacademy.springwebapp.exceptions;

public class ArticleNotFoundException extends RuntimeException {
  public ArticleNotFoundException(String message) {
    super(message);
  }
}
