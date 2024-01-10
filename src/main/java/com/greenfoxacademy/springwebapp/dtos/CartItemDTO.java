package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItemDTO {

  @JsonProperty("product_id")
  private Long productId;

  private String name;

  private int price;

  public CartItemDTO() {
  }

  public CartItemDTO(Long productId, String name, int price) {
    this.productId = productId;
    this.name = name;
    this.price = price;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }
}
