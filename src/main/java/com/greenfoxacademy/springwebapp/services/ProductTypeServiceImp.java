package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
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
}