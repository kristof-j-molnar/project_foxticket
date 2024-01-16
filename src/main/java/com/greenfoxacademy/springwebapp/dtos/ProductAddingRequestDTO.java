package com.greenfoxacademy.springwebapp.dtos;

public class ProductAddingRequestDTO {

  private Long productId;

  public ProductAddingRequestDTO() {
  }

  public ProductAddingRequestDTO(Long productId) {
    this.productId = productId;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }
}
