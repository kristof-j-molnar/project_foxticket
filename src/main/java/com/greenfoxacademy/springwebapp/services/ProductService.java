package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditResponseDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Product;

import java.util.Optional;

public interface ProductService {
  Optional<Product> findById(Long id);

  ProductListResponseDTO getAvailableProductsInDTO();

  Optional<Product> getProductById(Long productId);

  Optional<String> validateProductEditRequestDTO(ProductEditRequestDTO productEditRequestDTO);

  void save(Product product);

  ProductEditResponseDTO editProduct(Long productId, ProductEditRequestDTO requestDTO);
}