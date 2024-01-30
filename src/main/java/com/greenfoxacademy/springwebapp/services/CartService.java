package com.greenfoxacademy.springwebapp.services;

import org.springframework.security.core.Authentication;
import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.Cart;

public interface CartService {

  CartDTO getProductsInCartDTO(Integer id);

  void save(Cart cart);

  boolean isEmptyAddRequest(ProductAddingRequestDTO productDTO);

  MultipleProductsAddingResponseListDTO addProduct(ProductAddingRequestDTO productDTO);

  void removeProduct(Long itemId, Authentication auth);

  void clearCart(Authentication auth);
}