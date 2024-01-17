package com.greenfoxacademy.springwebapp.dtos;

public class ProductAddingResponseDTO {

  private Long cartId;

  private Long productId;

  private int amount;

  public ProductAddingResponseDTO() {
  }

  public ProductAddingResponseDTO(Long cartId, Long productId, int amount) {
    this.cartId = cartId;
    this.productId = productId;
    this.amount = amount;
  }

  public Long getCartId() {
    return cartId;
  }

  public void setCartId(Long cartId) {
    this.cartId = cartId;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
