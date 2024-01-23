package com.greenfoxacademy.springwebapp.dtos;

import java.util.ArrayList;
import java.util.List;

public class CartDTO {

  private final List<CartItemDTO> cart;

  public CartDTO() {
    cart = new ArrayList<>();
  }

  public List<CartItemDTO> getCart() {
    return cart;
  }

  public void add(CartItemDTO product) {
    cart.add(product);
  }
}

