package com.greenfoxacademy.springwebapp.dtos;

public class ProductAddingRequestDTO {

  private Long productId;

  private Integer amount;

  public ProductAddingRequestDTO() {
  }

  public ProductAddingRequestDTO(Long productId) {
    this.productId = productId;
    amount = 1;
  }

  public ProductAddingRequestDTO(Long productId, Integer amount) {
    this.productId = productId;
    this.amount = amount;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }
}

