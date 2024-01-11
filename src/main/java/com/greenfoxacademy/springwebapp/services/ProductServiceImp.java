package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

  private final ProductRepository productRepository;

  @Autowired
  public ProductServiceImp(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  @Override
  public Boolean findByName(String name) {
    return productRepository.findByName(name);
  }

  @Override
  public ProductListResponseDTO getAvailableProductsInDTO() {
    List<Product> productList = productRepository.findAll();
    ProductListResponseDTO productDTOs = new ProductListResponseDTO();
    for (Product product : productList) {
      productDTOs.add(new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getDuration(), product.getDescription(), product.getType().getName()));
    }
    return productDTOs;
  }

  @Override
  public Optional<Product> getProductById(Long productId) {
    return productRepository.findById(productId);
  }

  @Override
  public String validateProductEditDTO(ProductEditRequestDTO productEditRequestDTO) {
    if (productEditRequestDTO.getName().isBlank() || productEditRequestDTO.getName() == null) {
      return "Name";
    }
    if (productEditRequestDTO.getPrice() == null) {
      return "Price";
    }
    if (productEditRequestDTO.getDuration().isBlank() || productEditRequestDTO.getDuration() == null) {
      return "Duration";
    }
    if (productEditRequestDTO.getTypeId() == null) {
      return "Type id";
    }
    if (productEditRequestDTO.getDescription().isBlank() || productEditRequestDTO.getDescription() == null) {
      return "Description";
    }

    return null;
  }

  @Override
  public Product editProduct(Product product, ProductEditRequestDTO productEditRequestDTO) {
    return null;
  }
}