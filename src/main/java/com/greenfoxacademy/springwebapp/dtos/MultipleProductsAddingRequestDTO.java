package com.greenfoxacademy.springwebapp.dtos;

public class MultipleProductsAddingRequestDTO {

  private Long productId;

  private Integer amount;

  public MultipleProductsAddingRequestDTO() {
  }

  public MultipleProductsAddingRequestDTO(Long productId, Integer amount) {
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
