package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;

public interface ProductService {
  ProductListResponseDTO getAvailableProductsInDTO();

  CartDTO getProductsInCartDTO(Integer id);
}
