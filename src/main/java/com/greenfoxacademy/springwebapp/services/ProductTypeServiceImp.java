package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductTypeAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductTypeAddingResponseDTO;
import com.greenfoxacademy.springwebapp.exceptions.EmptyFieldsException;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductTypeServiceImp implements ProductTypeService {
  private final ProductTypeRepository productTypeRepository;

  @Autowired
  public ProductTypeServiceImp(ProductTypeRepository productTypeRepository) {
    this.productTypeRepository = productTypeRepository;
  }

  @Override
  public Optional<ProductType> findById(Long id) {
    return productTypeRepository.findById(id);
  }

  public ProductTypeAddingResponseDTO addProductType(ProductTypeAddingRequestDTO request) {
    if (request == null || request.getName() == null || request.getName().isEmpty()) {
      throw new EmptyFieldsException("Name is required");
    }
    if (productTypeRepository.existsByName(request.getName())) {
      throw new EntityExistsException("Product type name already exists");
    }
    ProductType newType = new ProductType(request.getName());
    productTypeRepository.save(newType);
    return new ProductTypeAddingResponseDTO(newType.getId(), newType.getName());
  }
}