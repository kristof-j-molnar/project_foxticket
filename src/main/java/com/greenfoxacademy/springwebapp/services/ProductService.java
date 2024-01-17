package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditResponseDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.exceptions.EmptyFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.ProductNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.ProductTypeNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.UniqueNameViolationException;
import com.greenfoxacademy.springwebapp.models.Product;

import java.util.Optional;

public interface ProductService {
  Optional<Product> findById(Long id);

  ProductListResponseDTO getAvailableProductsInDTO();

  Optional<Product> getProductById(Long productId);

  Optional<String> validateProductEditRequestDTO(ProductEditRequestDTO productEditRequestDTO);

  Product modifyProduct(Product product, ProductEditRequestDTO productEditRequestDTO) throws ProductTypeNotFoundException;

  void save(Product product);

  ProductEditResponseDTO getProductEditResponseDTO(Product editedProduct);

  ProductEditResponseDTO editProduct(Long productId, ProductEditRequestDTO requestDTO)
      throws EmptyFieldsException, ProductNotFoundException, ProductTypeNotFoundException, UniqueNameViolationException;
}