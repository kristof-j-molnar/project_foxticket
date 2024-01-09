package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;

public interface ProductService {
  ProductListResponseDTO getAvailableProductsInDTO();
}
