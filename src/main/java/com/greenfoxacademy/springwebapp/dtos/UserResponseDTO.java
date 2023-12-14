package com.greenfoxacademy.springwebapp.dtos;

public class UserResponseDTO {
  private Integer id;

  private String email;

  private String role;

  public UserResponseDTO(Integer id, String email, String role) {
    this.id = id;
    this.email = email;
    this.role = role;
  }

  public UserResponseDTO() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
