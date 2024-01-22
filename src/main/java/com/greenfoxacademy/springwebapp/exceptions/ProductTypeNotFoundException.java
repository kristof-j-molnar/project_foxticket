package com.greenfoxacademy.springwebapp.exceptions;

public class ProductTypeNotFoundException extends RuntimeException {
  public ProductTypeNotFoundException(String message) {
    super(message);
  }
}
