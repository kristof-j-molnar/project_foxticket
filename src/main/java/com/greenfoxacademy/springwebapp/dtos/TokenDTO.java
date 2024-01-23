package com.greenfoxacademy.springwebapp.dtos;

public class TokenDTO {
  private final String status;
  private final String token;

  public TokenDTO(String status, String token) {
    this.status = status;
    this.token = token;
  }

  public String getStatus() {
    return status;
  }

  public String getToken() {
    return token;
  }
}