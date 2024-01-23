package com.greenfoxacademy.springwebapp.dtos;

public class ConfirmationMessageDTO {

  private String confirmationMessage;

  public ConfirmationMessageDTO(String confirmationMessage) {
    this.confirmationMessage = confirmationMessage;
  }

  public String getConfirmationMessage() {
    return confirmationMessage;
  }
}