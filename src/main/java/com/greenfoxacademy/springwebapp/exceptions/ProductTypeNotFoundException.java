package com.greenfoxacademy.springwebapp.exceptions;

public class ProductTypeNotFoundException extends Throwable {
  public ProductTypeNotFoundException(String message) {
    super(message);
  }
}
