package com.greenfoxacademy.springwebapp.dtos;

public class UserLoginDTO {
  private final String email;
  private final String password;

  public UserLoginDTO(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}