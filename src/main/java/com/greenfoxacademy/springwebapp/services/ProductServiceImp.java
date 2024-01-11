package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartItemDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenfoxacademy.springwebapp.dtos.CartDTO;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

  private ProductRepository productRepository;

  @Autowired
  public ProductServiceImp(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public ProductListResponseDTO getAvailableProductsInDTO() {
    List<Product> productList = productRepository.findAll();
    ProductListResponseDTO productDTOs = new ProductListResponseDTO();
    for (Product product : productList) {
      productDTOs.add(new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getDuration(), product.getDescription(), product.getType().getName()));
    }
    return productDTOs;
  }
}
