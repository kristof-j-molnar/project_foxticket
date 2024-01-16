package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.models.Cart;

public interface CartService {

  CartDTO getProductsInCartDTO(Integer id);

  void save(Cart cart);
}
