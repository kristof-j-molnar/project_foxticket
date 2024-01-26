package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingResponseDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import org.springframework.security.core.Authentication;

public interface CartService {

  CartDTO getProductsInCartDTO(Integer id);

  boolean isEmptyAddRequest(ProductAddingRequestDTO productAddingRequestDTO);

  ProductAddingResponseDTO addProduct(User user, Product product);

  void removeProduct(Long itemId, Authentication auth);

  void clearCart(Authentication auth);
}