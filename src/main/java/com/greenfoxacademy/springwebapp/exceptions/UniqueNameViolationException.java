package com.greenfoxacademy.springwebapp.exceptions;

public class UniqueNameViolationException extends RuntimeException {
  public UniqueNameViolationException(String message) {
    super(message);
  }
}
