package com.greenfoxacademy.springwebapp.dtos;

import com.greenfoxacademy.springwebapp.models.ProductType;

public class ProductTypeAddingResponseDTO {

  private Long id;

  private String name;

  public ProductTypeAddingResponseDTO() {
  }

  public ProductTypeAddingResponseDTO(ProductType type) {
    id = type.getId();
    name = type.getName();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


}
