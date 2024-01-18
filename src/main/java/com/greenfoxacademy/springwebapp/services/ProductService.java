package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Product;

import java.util.Optional;

public interface ProductService {
  ProductListResponseDTO getAvailableProductsInDTO();

  Optional<Product> getProductById(Long productId);
}
