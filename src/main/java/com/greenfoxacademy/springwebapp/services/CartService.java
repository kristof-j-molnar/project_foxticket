package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;

public interface CartService {

  CartDTO getProductsInCartDTO(Integer id);
}
