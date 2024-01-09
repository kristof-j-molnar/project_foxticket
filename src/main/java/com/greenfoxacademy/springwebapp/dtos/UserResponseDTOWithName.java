package com.greenfoxacademy.springwebapp.dtos;

public class UserResponseDTOWithName {
  private Integer id;

  private String email;

  private String name;

  public UserResponseDTOWithName(Integer id, String name, String email) {
    this.id = id;
    this.email = name;
    this.name = email;
  }

  public UserResponseDTOWithName() {
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
