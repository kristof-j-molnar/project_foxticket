package com.greenfoxacademy.springwebapp.exceptions;

public class ProductNotFoundException extends Throwable {
  public ProductNotFoundException(String message) {
    super(message);
  }
}
