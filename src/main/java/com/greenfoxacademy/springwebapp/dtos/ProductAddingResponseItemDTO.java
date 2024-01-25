package com.greenfoxacademy.springwebapp.dtos;

public class ProductAddingResponseItemDTO {

  private Long id;

  private Long productId;

  public ProductAddingResponseItemDTO() {
  }

  public ProductAddingResponseItemDTO(Long id, Long productId) {
    this.id = id;
    this.productId = productId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }
}
