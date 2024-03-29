package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "product_types")
public class ProductType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "type", fetch = FetchType.LAZY)
  private List<Product> productList;

  public ProductType() {
    productList = new ArrayList<>();
  }

  public ProductType(String name) {
    this();
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Product> getProductList() {
    return productList;
  }

  public void setProductList(List<Product> productList) {
    this.productList = productList;
  }

  public void addProduct(Product product) {
    productList.add(product);
    product.setType(this);
  }
}
