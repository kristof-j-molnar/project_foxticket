package com.greenfoxacademy.springwebapp.dtos;

import com.greenfoxacademy.springwebapp.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListResponseDTO {

  private List<ProductDTO> products;

  public ProductListResponseDTO() {
    products = new ArrayList<>();
  }

  public List<ProductDTO> getProducts() {
    return products;
  }

  public void add(ProductDTO product) {
    products.add(product);
  }
}
