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

  @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "cart", fetch = FetchType.EAGER)
  private User user;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "cart_product",
      joinColumns = @JoinColumn(name = "cart_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id"))
  private List<Product> productList;

  public Cart() {
    productList = new ArrayList<>();
  }

  public Cart(User user) {
    this();
    addUser(user);
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

  public void addUser(User user) {
    this.user = user;
    user.setCart(this);
  }

  public void addProduct(Product product) {
    productList.add(product);
    product.addCart(this);
  }
}

