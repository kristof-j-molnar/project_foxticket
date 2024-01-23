package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;

public interface CartService {

  CartDTO getProductsInCartDTO(Integer id);

  void save(Cart cart);

  boolean isEmptyAddRequest(ProductAddingRequestDTO productAddingRequestDTO);

  ProductAddingResponseDTO addProduct(User user, Product product);

  boolean isEmptyMultipleAddRequest(MultipleProductsAddingRequestDTO productWithAmount);

  MultipleProductsAddingResponseListDTO addMultipleProduct(User user, Product product, Integer amount);
}
