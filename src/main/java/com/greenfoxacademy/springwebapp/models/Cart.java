package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "cart", fetch = FetchType.LAZY)
  private User user;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "cart_product",
      joinColumns = @JoinColumn(name = "cart_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id"))
  private List<Product> productList;

  public Cart() {
    productList = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Product> getProductList() {
    return productList;
  }

  public void setProductList(List<Product> productList) {
    this.productList = productList;
  }

  public void addProduct(Product product) {
    productList.add(product);
  }

  public void removeProduct(Product product) {
    productList.remove(product);
  }

  public Product getProductFromCart(Cart cart, Long productId) {
    for (Product product : cart.getProductList()) {
      if (product.getId().equals(productId)) {
        return product;
      }
    }
    return null;
  }
}