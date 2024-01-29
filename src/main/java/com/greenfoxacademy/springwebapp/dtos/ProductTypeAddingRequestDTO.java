package com.greenfoxacademy.springwebapp.dtos;

public class ProductTypeAddingRequestDTO {

  private String name;

  public ProductTypeAddingRequestDTO() {
  }

  public ProductTypeAddingRequestDTO(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
