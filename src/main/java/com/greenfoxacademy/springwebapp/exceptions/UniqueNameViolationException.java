package com.greenfoxacademy.springwebapp.exceptions;

public class UniqueNameViolationException extends Throwable {
  public UniqueNameViolationException(String message) {
    super(message);
  }
}
