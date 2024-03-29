package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductTypeAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductTypeAddingResponseDTO;
import com.greenfoxacademy.springwebapp.models.ProductType;

import java.util.Optional;

public interface ProductTypeService {
  Optional<ProductType> findById(Long id);

  ProductTypeAddingResponseDTO addProductType(ProductTypeAddingRequestDTO request);
}