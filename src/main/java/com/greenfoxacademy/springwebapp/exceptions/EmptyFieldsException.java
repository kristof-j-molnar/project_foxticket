package com.greenfoxacademy.springwebapp.exceptions;

public class EmptyFieldsException extends RuntimeException {
  public EmptyFieldsException(String message) {
    super(message);
  }
}