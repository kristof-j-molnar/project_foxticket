package com.greenfoxacademy.springwebapp.exceptions;

public class CartEmptyException  extends RuntimeException {
  public CartEmptyException(String message) {
    super(message);
  }
}