package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cartItems")
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinColumn(name = "product_id")
  private Product product;

  public CartItem() {
  }

  public CartItem(Product product, Cart cart) {
    this.product = product;
    this.cart = cart;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }
}