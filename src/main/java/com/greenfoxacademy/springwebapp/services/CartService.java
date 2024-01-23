package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.User;

public interface CartService {

  CartDTO getProductsInCartDTO(Integer id);

  void save(Cart cart);

  boolean isEmptyAddRequest(ProductAddingRequestDTO productDTO);

  ProductAddingResponseDTO addProduct(User user, ProductAddingRequestDTO productDTO);

  MultipleProductsAddingResponseListDTO addMultipleProduct(User user, ProductAddingRequestDTO productDTO);

}
