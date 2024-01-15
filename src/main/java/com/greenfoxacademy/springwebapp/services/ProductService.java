package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditResponseDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Product;

import java.util.Optional;

public interface ProductService {
  Optional<Product> findById(Long id);

  Boolean existsByName(String name);

  ProductListResponseDTO getAvailableProductsInDTO();

  Optional<Product> getProductById(Long productId);

  String validateProductEditDTO(ProductEditRequestDTO productEditRequestDTO);

  Product editProduct(Product product, ProductEditRequestDTO productEditRequestDTO);

  void save(Product product);

  ProductEditResponseDTO getProductEditResponseDTO(Product editedProduct);
}