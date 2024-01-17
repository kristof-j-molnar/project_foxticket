package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;

public interface CartService {

  CartDTO getProductsInCartDTO(Integer id);

  void save(Cart cart);

  int getAmount(Cart cart, Product product);
}
